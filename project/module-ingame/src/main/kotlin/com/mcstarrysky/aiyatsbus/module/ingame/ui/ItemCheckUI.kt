@file:Suppress("UNCHECKED_CAST")

/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.mcstarrysky.aiyatsbus.module.ingame.ui

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.asLang
import com.mcstarrysky.aiyatsbus.core.data.CheckType
import com.mcstarrysky.aiyatsbus.core.etsAvailable
import com.mcstarrysky.aiyatsbus.core.fixedEnchants
import com.mcstarrysky.aiyatsbus.core.util.toBuiltComponent
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.*
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature.util.MenuFunctionBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import com.mcstarrysky.aiyatsbus.core.util.variable
import com.mcstarrysky.aiyatsbus.core.util.variables
import taboolib.module.chat.component
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.PageableChest
import com.mcstarrysky.aiyatsbus.module.ingame.ui.ItemCheckUI.CheckMode.FIND
import com.mcstarrysky.aiyatsbus.module.ingame.ui.ItemCheckUI.CheckMode.LOAD
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.UIType
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.record
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.chat.Source
import taboolib.platform.util.modifyMeta

@MenuComponent("ItemCheck")
object ItemCheckUI {

    @Config("core/ui/item_check.yml", autoReload = true)
    private lateinit var source: Configuration
    private lateinit var config: MenuConfiguration

    fun reload() {
        source.reload()
        config = MenuConfiguration(source)
    }

    @Awake(LifeCycle.ENABLE)
    fun init() {
        source.onReload {
            config = MenuConfiguration(source)
        }
    }

    enum class CheckMode(private val node: String) {
        FIND("ui-check-mode-find"),
        LOAD("ui-check-mode-load");

        fun ifCurrent(current: Boolean): String {
            return if (current) "$node-current" else node
        }
    }

    fun open(player: Player, item: ItemStack? = null, mode: CheckMode) {
        player.record(UIType.ITEM_CHECK, "item" to item, "mode" to mode)
        player.openMenu<PageableChest<Pair<AiyatsbusEnchantment, Int>>>(config.title().component().buildColored().toLegacyText()) {
            val (shape, templates) = config
            rows(shape.rows)
            val slots = shape["ItemCheck:enchant"].toList()
            slots(slots)
            elements {
                when (mode) {
                    FIND -> item?.etsAvailable(CheckType.ANVIL, player)?.map { it to it.basicData.maxLevel } ?: emptyList()
                    LOAD -> item.fixedEnchants.toList()
                }
            }

            load(shape, templates, player, "ItemCheck:enchant", "ItemCheck:item", "ItemCheck:mode", "Previous", "Next")
            pages(shape, templates)

            val template = templates.require("ItemCheck:enchant")
            onGenerate(async = true) { _, element, index, slot ->
                template(slot, index) {
                    this["enchantPair"] = element
                    this["player"] = player
                    this["item"] = item
                }
            }
            onClick { event, element -> templates[event.rawSlot]?.handle(this, event, "enchant" to element.first, "level" to element.second) }

            item?.let { setSlots(shape, templates, "ItemCheck:item", listOf(), "item" to it, "mode" to mode) }
            setSlots(shape, templates, "ItemCheck:mode", listOf(), "item" to (item ?: ""), "mode" to mode, "player" to player)

            onClick { event ->
                event.isCancelled = true
                if (event.rawSlot !in shape && event.currentItem?.type != Material.AIR)
                    open(player, event.currentItem, mode)
            }
        }
    }

    @MenuComponent
    private val enchant = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val enchantPair = args["enchantPair"] as Pair<AiyatsbusEnchantment, Int>
            val enchant = enchantPair.first
            val level = enchantPair.second
            val holders = enchant.displayer.holders(level, args["player"] as Player, args["item"] as ItemStack)
            icon.variables { variable -> listOf(holders[variable] ?: "") }
                .modifyMeta<ItemMeta> { lore = lore.toBuiltComponent().map(Source::toLegacyText) }
                .skull(enchant.rarity.skull)
        }
        onClick { (_, _, _, event, args) ->
            EnchantInfoUI.open(event.clicker, args["enchant"] as AiyatsbusEnchantment, args["level"] as Int)
        }
    }

    @MenuComponent
    private val mode = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val player = args["player"] as Player
            val current = args["mode"] as CheckMode
            icon.variable("modes", CheckMode.values().map { player.asLang(it.ifCurrent(current == it)) })
        }
        onClick { (_, _, _, event, args) ->
            val current = args["mode"] as CheckMode
            val entries = CheckMode.values()
            var index = entries.indexOf(current)
            if (index >= entries.size - 1) index = -1
            open(event.clicker, args["item"] as? ItemStack, entries[index + 1])
        }
    }

    @MenuComponent
    private val item = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) -> args["item"] as? ItemStack ?: icon }
        onClick { (_, _, _, event, args) -> open(event.clicker, null, args["mode"] as CheckMode) }
    }
}