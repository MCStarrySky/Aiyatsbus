package com.mcstarrysky.aiyatsbus.module.ingame.ui

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.FilterStatement
import com.mcstarrysky.aiyatsbus.core.FilterType
import com.mcstarrysky.aiyatsbus.core.aiyatsbusTargets
import com.mcstarrysky.aiyatsbus.core.data.Target
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.MenuComponent
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature.util.MenuFunctionBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.variables
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.load
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.pages
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.skull
import taboolib.module.chat.component
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.PageableChest
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.UIType
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.record
import kotlin.collections.set

@MenuComponent("FilterTarget")
object FilterTargetUI {

    @Config("core/ui/filter_target.yml")
    private lateinit var source: Configuration
    private lateinit var config: MenuConfiguration

    fun reload() {
        source.reload()
        config = MenuConfiguration(source)
    }

    fun open(player: Player) {
        player.record(UIType.FILTER_TARGET)
        player.openMenu<PageableChest<Target>>(config.title().component().buildColored().toRawMessage()) {
            virtualize()

            val (shape, templates) = config
            rows(shape.rows)
            val slots = shape["FilterTarget:filter"].toList()
            slots(slots)
            elements { aiyatsbusTargets.values.toList() }

            load(shape, templates, player, "FilterTarget:filter", "Previous", "Next")
            pages(shape, templates)

            val template = templates.require("FilterTarget:filter")
            onGenerate { _, element, index, slot ->
                template(slot, index) {
                    this["target"] = element
                    this["player"] = player
                }
            }
            onClick { event, element -> templates[event.rawSlot]?.handle(this, event, "target" to element) }
        }
    }

    @MenuComponent
    private val filter = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val target = args["target"] as Target
            val player = args["player"] as Player

            when (Aiyatsbus.api().getEnchantmentFilter().getStatement(player, FilterType.TARGET, target.id)) {
                FilterStatement.ON -> icon.type = Material.LIME_STAINED_GLASS_PANE
                FilterStatement.OFF -> icon.type = Material.RED_STAINED_GLASS_PANE
                else -> {}
            }

            icon.variables {
                when (it) {
                    "name" -> listOf(target.name)
                    "amount" -> listOf((Aiyatsbus.api().getEnchantmentManager().getByIDs().values.count { it.targets.contains(target) }).toString())
                    else -> emptyList()
                }
            }.skull(target.skull)
        }

        onClick { (_, _, _, event, args) ->
            val clickType = event.virtualEvent().clickType
            val player = event.clicker
            val target = args["target"] as Target

            when (clickType) {
                ClickType.LEFT, ClickType.RIGHT -> {
                    Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.TARGET, target)
                    Aiyatsbus.api().getEnchantmentFilter().addFilter(
                        player, FilterType.TARGET, target.id,
                        when (clickType) {
                            ClickType.RIGHT -> FilterStatement.OFF
                            else -> FilterStatement.ON
                        }
                    )
                    open(player)
                }

                ClickType.MIDDLE -> {
                    Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.TARGET, target)
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
            Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.TARGET)
            open(player)
        }
    }
}