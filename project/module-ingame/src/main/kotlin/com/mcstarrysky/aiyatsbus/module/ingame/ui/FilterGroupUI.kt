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

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.FilterStatement
import com.mcstarrysky.aiyatsbus.core.FilterType
import com.mcstarrysky.aiyatsbus.core.aiyatsbusGroups
import com.mcstarrysky.aiyatsbus.core.data.registry.Group
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.MenuComponent
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature.util.MenuFunctionBuilder
import com.mcstarrysky.aiyatsbus.core.util.variables
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
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import kotlin.collections.set

@MenuComponent("FilterGroup")
object FilterGroupUI {

    @Config("core/ui/filter_group.yml", autoReload = true)
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
        player.record(UIType.FILTER_GROUP)
        player.openMenu<PageableChest<Group>>(config.title().component().buildColored().toLegacyText()) {
//            virtualize()
            val (shape, templates) = config
            rows(shape.rows)
            val slots = shape["FilterGroup:filter"].toList()
            slots(slots)
            elements { aiyatsbusGroups.values.toList() }

            load(shape, templates, player, "FilterGroup:filter", "Previous", "Next")
            pages(shape, templates)

            val template = templates.require("FilterGroup:filter")
            onGenerate(async = true) { _, element, index, slot ->
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
            }.skull(group.skull)
        }

        onClick { (_, _, _, event, args) ->
            val clickType = event.clickEvent().click
            val player = event.clicker
            val group = args["group"] as Group

            when (clickType) {
                ClickType.LEFT, ClickType.RIGHT -> {
                    Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.GROUP, group.name)
                    Aiyatsbus.api().getEnchantmentFilter().addFilter(
                        player, FilterType.GROUP, group.name,
                        when (clickType) {
                            ClickType.RIGHT -> FilterStatement.OFF
                            else -> FilterStatement.ON
                        }
                    )
                    open(player)
                }

                ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT -> {
                    Aiyatsbus.api().getEnchantmentFilter().clearFilter(player, FilterType.GROUP, group.name)
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