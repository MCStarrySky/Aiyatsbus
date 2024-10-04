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
package com.mcstarrysky.aiyatsbus.core.data.trigger.event

import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.core.util.enumOf
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.platform.event.EventPriority
import taboolib.library.configuration.ConfigurationSection

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.data.trigger.event.EventMapping
 *
 * @author mical
 * @since 2024/7/18 00:29
 */
class EventMapping @JvmOverloads constructor(
    private val root: ConfigurationSection,

    val clazz: String = root.getString("class")!!,

    val slots: List<EquipmentSlot> = if (root.isList("slots")) root.getStringList("slots")
        .mapNotNull { it.enumOf<EquipmentSlot>() } else listOfNotNull(
        root.getString("slots").enumOf<EquipmentSlot>()
    ),

    val playerReference: String? = root.getString("playerReference") ?: root.getString("player")
    ?: root.getString("player-reference"),

    val itemReference: String? = root.getString("itemReference") ?: root.getString("item")
    ?: root.getString("item-reference"),

    val eventPriority: EventPriority = root.getString("priority").enumOf<EventPriority>() ?: EventPriority.HIGHEST,

    val ignoreCancelled: Boolean = (root.getString("ignoreCancelled")
        ?: root.getString("ignore-cancelled")).coerceBoolean(true)
) {

    operator fun component1() = clazz
    operator fun component2() = slots
    operator fun component3() = playerReference
    operator fun component4() = itemReference
    operator fun component5() = eventPriority

    operator fun component6() = ignoreCancelled
}