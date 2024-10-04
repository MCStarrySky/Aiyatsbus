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
package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.Event
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.PropertyEvent
 *
 * @author mical
 * @since 2024/3/10 13:26
 */
@AiyatsbusProperty(
    id = "event",
    bind = Event::class
)
class PropertyEvent : AiyatsbusGenericProperty<Event>("event") {

    override fun readProperty(instance: Event, key: String): OpenResult {
        val property: Any? = when (key) {
            "eventName", "event-name", "name" -> instance.eventName
            "isAsynchronous", "is-asynchronous", "asynchronous", "is-async", "async" -> instance.isAsynchronous
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Event, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}