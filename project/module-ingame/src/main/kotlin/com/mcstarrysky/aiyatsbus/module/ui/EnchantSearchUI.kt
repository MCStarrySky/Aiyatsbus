@file:Suppress("UNCHECKED_CAST")

package com.mcstarrysky.aiyatsbus.module.ui

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.module.ui.internal.*
import com.mcstarrysky.aiyatsbus.module.ui.internal.config.MenuConfiguration
import com.mcstarrysky.aiyatsbus.module.ui.internal.feature.util.MenuFunctionBuilder
import com.mcstarrysky.aiyatsbus.module.ui.internal.function.variable
import com.mcstarrysky.aiyatsbus.module.ui.internal.function.variables
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import taboolib.common.platform.function.submit
import taboolib.module.chat.component
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.ui.openMenu
import taboolib.platform.util.nextChat
import taboolib.module.ui.type.PageableChest
import taboolib.platform.util.sendLang
import com.mcstarrysky.aiyatsbus.module.ui.internal.UIType
import com.mcstarrysky.aiyatsbus.module.ui.internal.record
import kotlin.collections.set

@MenuComponent("EnchantSearch")
object EnchantSearchUI {

    @Config("core/ui/enchant_search.yml")
    private lateinit var source: Configuration
    private lateinit var config: MenuConfiguration

    fun reload() {
        source.reload()
        config = MenuConfiguration(source)
    }

    fun open(player: Player) {
        player.record(UIType.ENCHANT_SEARCH)
        player.openMenu<PageableChest<AiyatsbusEnchantment>>(config.title().component().buildColored().toRawMessage()) {
            virtualize()

            val (shape, templates) = config
            rows(shape.rows)
            val slots = shape["EnchantSearch:enchant"].toList()
            slots(slots)
            elements { Aiyatsbus.api().getEnchantmentFilter().filter(player.filters) }

            load(
                shape, templates, player,
                "EnchantSearch:enchant", "EnchantSearch:filter_rarity", "EnchantSearch:filter_target",
                "EnchantSearch:filter_group", "EnchantSearch:filter_string", "Previous", "Next"
            )
            pages(shape, templates)

            val template = templates.require("EnchantSearch:enchant")
            onGenerate { _, element, index, slot -> template(slot, index) { this["enchant"] = element } }
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
            val holders = enchant.displayer.holders(enchant.basicData.maxLevel)
            icon.variables { variable -> listOf(holders[variable] ?: "") }
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
            val clickType = event.virtualEvent().clickType
            val player = event.clicker
            if (clickType == ClickType.MIDDLE) {
                Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.RARITY)
                open(player)
            } else FilterRarityUI.open(event.clicker)
        }
    }

    @MenuComponent
    private val filter_target = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) -> icon.variable("targets", args["filters"] as List<String>) }
        onClick { (_, _, _, event, _) ->
            val clickType = event.virtualEvent().clickType
            val player = event.clicker
            if (clickType == ClickType.MIDDLE) {
                Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.TARGET)
                open(player)
            } else FilterTargetUI.open(event.clicker)
        }
    }

    @MenuComponent
    private val filter_group = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) -> icon.variable("groups", args["filters"] as List<String>) }
        onClick { (_, _, _, event, _) ->
            val clickType = event.virtualEvent().clickType
            val player = event.clicker
            if (clickType == ClickType.MIDDLE) {
                Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.GROUP)
                open(player)
            } else FilterGroupUI.open(event.clicker)
        }
    }

    @MenuComponent
    private val filter_string = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) -> icon.variable("strings", args["filters"] as List<String>) }
        onClick { (_, _, _, event, _) ->
            val clickType = event.virtualEvent().clickType
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

                ClickType.MIDDLE -> {
                    Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.STRING)
                    open(player)
                }

                else -> {}
            }
        }
    }
}