package com.mcstarrysky.aiyatsbus.module.ui

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.FilterStatement
import com.mcstarrysky.aiyatsbus.core.FilterType
import com.mcstarrysky.aiyatsbus.core.data.Group
import com.mcstarrysky.aiyatsbus.module.ui.internal.MenuComponent
import com.mcstarrysky.aiyatsbus.module.ui.internal.config.MenuConfiguration
import com.mcstarrysky.aiyatsbus.module.ui.internal.feature.util.MenuFunctionBuilder
import com.mcstarrysky.aiyatsbus.module.ui.internal.function.variables
import com.mcstarrysky.aiyatsbus.module.ui.internal.load
import com.mcstarrysky.aiyatsbus.module.ui.internal.pages
import com.mcstarrysky.aiyatsbus.module.ui.internal.skull
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import taboolib.module.chat.component
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.PageableChest
import com.mcstarrysky.aiyatsbus.module.ui.internal.UIType
import com.mcstarrysky.aiyatsbus.module.ui.internal.record
import kotlin.collections.set

@MenuComponent("FilterGroup")
object FilterGroupUI {

    @Config("core/ui/filter_group.yml")
    private lateinit var source: Configuration
    private lateinit var config: MenuConfiguration

    fun reload() {
        source.reload()
        config = MenuConfiguration(source)
    }

    fun open(player: Player) {
        player.record(UIType.FILTER_GROUP)
        player.openMenu<PageableChest<Group>>(config.title().component().buildColored().toRawMessage()) {
            virtualize()
            val (shape, templates) = config
            rows(shape.rows)
            val slots = shape["FilterGroup:filter"].toList()
            slots(slots)
            elements { Group.groups.values.toList() }

            load(shape, templates, player, "FilterGroup:filter", "Previous", "Next")
            pages(shape, templates)

            val template = templates.require("FilterGroup:filter")
            onGenerate { _, element, index, slot ->
                template(slot, index) {
                    this["group"] = element
                    this["player"] = player
                }
            }
            onClick { event, element -> templates[event.rawSlot]?.handle(this, event, "group" to element) }
        }
    }

    @MenuComponent
    private val filter = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val group = args["group"] as Group
            val player = args["player"] as Player

            when (Aiyatsbus.api().getEnchantmentFilter().getStatement(player, FilterType.GROUP, group.name)) {
                FilterStatement.ON -> icon.type = Material.LIME_STAINED_GLASS_PANE
                FilterStatement.OFF -> icon.type = Material.RED_STAINED_GLASS_PANE
                else -> {}
            }

            icon.variables {
                when (it) {
                    "name" -> listOf(group.name)
                    "amount" -> listOf(group.enchantments.size.toString())
                    else -> emptyList()
                }
            }.skull(group.skullBase64)
        }

        onClick { (_, _, _, event, args) ->
            val clickType = event.virtualEvent().clickType
            val player = event.clicker
            val group = args["group"] as Group

            when (clickType) {
                ClickType.LEFT, ClickType.RIGHT -> {
                    Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.GROUP, group)
                    Aiyatsbus.api().getEnchantmentFilter().addFilter(
                        player, FilterType.GROUP, group.name,
                        when (clickType) {
                            ClickType.RIGHT -> FilterStatement.OFF
                            else -> FilterStatement.ON
                        }
                    )
                    open(player)
                }

                ClickType.MIDDLE -> {
                    Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.GROUP, group)
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
            Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.GROUP)
            open(player)
        }
    }
}