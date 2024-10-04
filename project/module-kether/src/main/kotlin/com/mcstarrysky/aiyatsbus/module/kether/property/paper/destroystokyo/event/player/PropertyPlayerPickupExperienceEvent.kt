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
package com.mcstarrysky.aiyatsbus.module.kether.property.paper.destroystokyo.event.player

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.paper.destroystokyo.event.player.PropertyPlayerPickupExperienceEvent
 *
 * @author mical
 * @since 2024/8/19 08:41
 */
@AiyatsbusProperty(
    id = "player-pickup-experience-event",
    bind = PlayerPickupExperienceEvent::class
)
class PropertyPlayerPickupExperienceEvent : AiyatsbusGenericProperty<PlayerPickupExperienceEvent>("player-pickup-experience-event") {

    override fun readProperty(instance: PlayerPickupExperienceEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "experienceOrb", "experience-orb" -> instance.experienceOrb
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: PlayerPickupExperienceEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}