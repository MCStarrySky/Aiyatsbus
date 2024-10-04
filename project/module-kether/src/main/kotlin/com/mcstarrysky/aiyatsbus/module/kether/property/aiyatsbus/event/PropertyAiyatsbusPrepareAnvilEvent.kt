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
package com.mcstarrysky.aiyatsbus.module.kether.property.aiyatsbus.event

import com.mcstarrysky.aiyatsbus.core.event.AiyatsbusPrepareAnvilEvent
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveItemStack
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.aiyatsbus.event.PropertyAiyatsbusPrepareAnvilEvent
 *
 * @author mical
 * @since 2024/5/6 22:00
 */
@AiyatsbusProperty(
    id = "aiyatsbus-prepare-anvil-event",
    bind = AiyatsbusPrepareAnvilEvent::class
)
class PropertyAiyatsbusPrepareAnvilEvent : AiyatsbusGenericProperty<AiyatsbusPrepareAnvilEvent>("aiyatsbus-prepare-anvil-event") {

    override fun readProperty(instance: AiyatsbusPrepareAnvilEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "player" -> instance.player
            "left" -> instance.left
            "right" -> instance.right
            "result" -> instance.result
            "name" -> instance.name
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: AiyatsbusPrepareAnvilEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "result" -> instance.result = value?.liveItemStack
            "name" -> instance.name = value?.toString()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}