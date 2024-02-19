@file:Suppress("UNCHECKED_CAST")

package com.mcstarrysky.aiyatsbus.module.ui

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.data.CheckType
import com.mcstarrysky.aiyatsbus.core.etsAvailable
import com.mcstarrysky.aiyatsbus.core.fixedEnchants
import com.mcstarrysky.aiyatsbus.core.mechanism.Reloadable
import com.mcstarrysky.aiyatsbus.module.ui.internal.*
import com.mcstarrysky.aiyatsbus.module.ui.internal.config.MenuConfiguration
import com.mcstarrysky.aiyatsbus.module.ui.internal.feature.util.MenuFunctionBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import com.mcstarrysky.aiyatsbus.module.ui.internal.function.variable
import com.mcstarrysky.aiyatsbus.module.ui.internal.function.variables
import taboolib.module.chat.component
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.PageableChest
import com.mcstarrysky.aiyatsbus.module.ui.ItemCheckUI.CheckMode.FIND
import com.mcstarrysky.aiyatsbus.module.ui.ItemCheckUI.CheckMode.LOAD
import com.mcstarrysky.aiyatsbus.module.ui.internal.UIType
import com.mcstarrysky.aiyatsbus.module.ui.internal.record

@MenuComponent("ItemCheck")
object ItemCheckUI {

    @Config("ui/item_check.yml")
    private lateinit var source: Configuration
    private lateinit var config: MenuConfiguration

    @Reloadable
    fun reload() {
        source.reload()
        config = MenuConfiguration(source)
    }

    enum class CheckMode(val display: String) {
        FIND("搜索可用附魔"),
        LOAD("读取物品附魔")
    }

    fun open(player: Player, item: ItemStack? = null, mode: CheckMode) {
        player.record(UIType.ITEM_CHECK, "item" to item, "mode" to mode)
        player.openMenu<PageableChest<Pair<AiyatsbusEnchantment, Int>>>(config.title().component().buildColored().toRawMessage()) {
            virtualize()

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
            onGenerate { _, element, index, slot ->
                template(slot, index) {
                    this["enchantPair"] = element
                    this["player"] = player
                    this["item"] = item
                }
            }
            onClick { event, element -> templates[event.rawSlot]?.handle(this, event, "enchant" to element.first, "level" to element.second) }

            item?.let { setSlots(shape, templates, "ItemCheck:item", listOf(), "item" to it, "mode" to mode) }
            setSlots(shape, templates, "ItemCheck:mode", listOf(), "item" to (item ?: ""), "mode" to mode)

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
                .skull(enchant.rarity.skull)
        }
        onClick { (_, _, _, event, args) ->
            EnchantInfoUI.open(event.clicker, args["enchant"] as AiyatsbusEnchantment, args["level"] as Int)
        }
    }

    @MenuComponent
    private val mode = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val current = args["mode"] as CheckMode
            icon.variable("modes", CheckMode.values().map {
                if (current == it) "&a${it.display}"
                else "&7${it.display}"
            })
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