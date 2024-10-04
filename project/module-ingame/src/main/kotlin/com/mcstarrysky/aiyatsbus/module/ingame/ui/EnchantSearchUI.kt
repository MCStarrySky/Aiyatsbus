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

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.util.toBuiltComponent
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.*
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature.util.MenuFunctionBuilder
import com.mcstarrysky.aiyatsbus.core.util.variable
import com.mcstarrysky.aiyatsbus.core.util.variables
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import taboolib.common.platform.function.submit
import taboolib.module.chat.component
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.ui.openMenu
import taboolib.platform.util.nextChat
import taboolib.module.ui.type.PageableChest
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.UIType
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.record
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.chat.Source
import taboolib.platform.util.modifyMeta
import kotlin.collections.set

@MenuComponent("EnchantSearch")
object EnchantSearchUI {

    @Config("core/ui/enchant_search.yml", autoReload = true)
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

    fun open(player: Player) {
        player.record(UIType.ENCHANT_SEARCH)
        player.openMenu<PageableChest<AiyatsbusEnchantment>>(config.title().component().buildColored().toLegacyText()) {
//            virtualize()

            val (shape, templates) = config
            rows(shape.rows)
            val slots = shape["EnchantSearch:enchant"].toList()
            slots(slots)
            elements { Aiyatsbus.api().getEnchantmentFilter().filter(player.filters).filter { it.basicData.enable } }

            load(
                shape, templates, player,
                "EnchantSearch:enchant", "EnchantSearch:filter_rarity", "EnchantSearch:filter_target",
                "EnchantSearch:filter_group", "EnchantSearch:filter_string", "Previous", "Next"
            )
            pages(shape, templates)

            val template = templates.require("EnchantSearch:enchant")
            onGenerate(async = true) { _, element, index, slot ->
                template(slot, index) {
                    this["enchant"] = element
                    this["player"] = player
                }
            }
            onClick { event, element -> templates[event.rawSlot]?.handle(this, event, "element" to element) }

            FilterType.values().forEach {
                setSlots(
                    shape, templates, "EnchantSearch:filter_${it.toString().lowercase()}", listOf(),
                    "filters" to Aiyatsbus.api().getEnchantmentFilter().generateLore(it, player)
                )
            }
        }
    }

    @MenuComponent
    private val enchant = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val enchant = args["enchant"] as AiyatsbusEnchantment
            val player = args["player"] as Player
            val holders = enchant.displayer.holders(enchant.basicData.maxLevel, player, enchant.book())
            icon.variables { variable -> listOf(holders[variable] ?: "") }
                .modifyMeta<ItemMeta> { lore = lore.toBuiltComponent().map(Source::toLegacyText) }
                .skull(enchant.rarity.skull)
        }
        onClick { (_, _, _, event, args) ->
            EnchantInfoUI.open(event.clicker, args["element"] as AiyatsbusEnchantment)
        }
    }

    @MenuComponent
    private val filter_rarity = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) -> icon.variable("rarities", args["filters"] as List<String>) }
        onClick { (_, _, _, event, _) ->
            val clickType = event.clickEvent().click
            val player = event.clicker
            if (clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
                Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.RARITY)
                open(player)
            } else FilterRarityUI.open(event.clicker)
        }
    }

    @MenuComponent
    private val filter_target = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) -> icon.variable("targets", args["filters"] as List<String>) }
        onClick { (_, _, _, event, _) ->
            val clickType = event.clickEvent().click
            val player = event.clicker
            if (clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
                Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.TARGET)
                open(player)
            } else FilterTargetUI.open(event.clicker)
        }
    }

    @MenuComponent
    private val filter_group = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) -> icon.variable("groups", args["filters"] as List<String>) }
        onClick { (_, _, _, event, _) ->
            val clickType = event.clickEvent().click
            val player = event.clicker
            if (clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
                Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.GROUP)
                open(player)
            } else FilterGroupUI.open(event.clicker)
        }
    }

    @MenuComponent
    private val filter_string = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) -> icon.variable("strings", args["filters"] as List<String>) }
        onClick { (_, _, _, event, _) ->
            val clickType = event.clickEvent().click
            val player = event.clicker
            when (clickType) {
                ClickType.LEFT, ClickType.RIGHT -> {
                    player.closeInventory()
                    player.sendLang("messages-menu-search-input")
                    player.nextChat {
                        player.sendLang("messages-menu-search-input_finish")
                        Aiyatsbus.api().getEnchantmentFilter().addFilter(
                            player, FilterType.STRING, it,
                            when (clickType) {
                                ClickType.RIGHT -> FilterStatement.OFF
                                else -> FilterStatement.ON
                            }
                        )
                        submit { open(player) }
                    }
                }

                ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT -> {
                    Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.STRING)
                    open(player)
                }

                else -> {}
            }
        }
    }
}