package com.mcstarrysky.aiyatsbus.module.ingame.ui

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.data.Rarity
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.MenuComponent
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature.util.MenuFunctionBuilder
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.variables
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.load
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.pages
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.skull
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import taboolib.module.chat.component
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.PageableChest
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.UIType
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.record
import kotlin.collections.set

@MenuComponent("FilterRarity")
object FilterRarityUI {

    @Config("core/ui/filter_rarity.yml")
    private lateinit var source: Configuration
    private lateinit var config: MenuConfiguration

    fun reload() {
        source.reload()
        config = MenuConfiguration(source)
    }

    fun open(player: Player) {
        player.record(UIType.FILTER_RARITY)
        player.openMenu<PageableChest<Rarity>>(config.title().component().buildColored().toRawMessage()) {
            virtualize()
            val (shape, templates) = config
            rows(shape.rows)
            val slots = shape["FilterRarity:filter"].toList()
            slots(slots)
            elements { aiyatsbusRarities.values.toList() }

            load(shape, templates, player, "FilterRarity:filter", "Previous", "Next")
            pages(shape, templates)

            val template = templates.require("FilterRarity:filter")
            onGenerate { _, element, index, slot ->
                template(slot, index) {
                    this["rarity"] = element
                    this["player"] = player
                }
            }
            onClick { event, element -> templates[event.rawSlot]?.handle(this, event, "rarity" to element) }
        }
    }

    @MenuComponent
    private val filter = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val rarity = args["rarity"] as Rarity
            val player = args["player"] as Player

            when (Aiyatsbus.api().getEnchantmentFilter().getStatement(player, FilterType.RARITY, rarity.id)) {
                FilterStatement.ON -> icon.type = Material.LIME_STAINED_GLASS_PANE
                FilterStatement.OFF -> icon.type = Material.RED_STAINED_GLASS_PANE
                else -> {}
            }

            icon.variables {
                when (it) {
                    "name", "rarity_display" -> listOf(rarity.displayName(rarity.name))
                    "amount" -> listOf(aiyatsbusEts(rarity).size.toString())
                    else -> emptyList()
                }
            }.skull(rarity.skull)
        }

        onClick { (_, _, _, event, args) ->
            val clickType = event.virtualEvent().clickType
            val player = event.clicker
            val rarity = args["rarity"] as Rarity

            when (clickType) {
                ClickType.LEFT, ClickType.RIGHT -> {
                    Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.RARITY, rarity.id)
                    Aiyatsbus.api().getEnchantmentFilter().addFilter(
                        player, FilterType.RARITY, rarity.id,
                        when (clickType) {
                            ClickType.RIGHT -> FilterStatement.OFF
                            else -> FilterStatement.ON
                        }
                    )
                    open(player)
                }

                ClickType.MIDDLE -> {
                    Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.RARITY, rarity.id)
                    open(player)
                }

                else -> {}
            }
        }
    }

    @MenuComponent
    private val reset = MenuFunctionBuilder {
        onClick { (_, _, _, event, _) ->
            val player = event.clicker
            Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.RARITY)
            open(player)
        }
    }
}