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

import com.mcstarrysky.aiyatsbus.core.event.AiyatsbusBowChargeEvent
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.aiyatsbus.event.PropertyAiyatsbusBowChargeEventPrepare
 *
 * @author mical
 * @since 2024/9/19 22:21
 */
@AiyatsbusProperty(
    id = "aiyatsbus-bow-charge-event-released",
    bind = AiyatsbusBowChargeEvent.Released::class
)
class PropertyAiyatsbusBowChargeEventReleased : AiyatsbusGenericProperty<AiyatsbusBowChargeEvent.Released>("aiyatsbus-bow-charge-event-released") {

    override fun readProperty(instance: AiyatsbusBowChargeEvent.Released, key: String): OpenResult {
        val property: Any? = when (key) {
            "player" -> instance.player
            "itemStack", "item-stack", "item" -> instance.itemStack
            "hand" -> instance.hand.name
            // TODO
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: AiyatsbusBowChargeEvent.Released, key: String, value: Any?): OpenResult {
        when (key) {
            // TODO
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}