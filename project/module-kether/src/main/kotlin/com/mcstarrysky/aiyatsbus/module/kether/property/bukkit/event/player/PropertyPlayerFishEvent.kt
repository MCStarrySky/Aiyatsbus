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
package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player

import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.player.PlayerFishEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player.PropertyPlayerFishEvent
 *
 * @author yanshiqwq
 * @since 2024/4/4 09:14
 */
@AiyatsbusProperty(
    id = "player-fish-event",
    bind = PlayerFishEvent::class
)
class PropertyPlayerFishEvent : AiyatsbusGenericProperty<PlayerFishEvent>("player-fish-event") {

    override fun readProperty(instance: PlayerFishEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "caught" -> instance.caught
            "hook" -> instance.hook
            "expToDrop", "exp-to-drop", "exp" -> instance.expToDrop
            "hand" -> instance.hand
            "state" -> instance.state.name
            "isFishing", "is-fishing" -> instance.state == PlayerFishEvent.State.FISHING
            "isCaughtFish", "is-caught-fish" -> instance.state == PlayerFishEvent.State.CAUGHT_FISH
            "isCaughtEntity", "is-caught-entity" -> instance.state == PlayerFishEvent.State.CAUGHT_ENTITY
            "isInGround", "is-in-ground" -> instance.state == PlayerFishEvent.State.IN_GROUND
            "isFailedAttempt", "is-failed-attempt" -> instance.state == PlayerFishEvent.State.FAILED_ATTEMPT
            "isReelIn", "is-reel-in" -> instance.state == PlayerFishEvent.State.REEL_IN
            "isBite", "is-bite" -> instance.state == PlayerFishEvent.State.BITE
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: PlayerFishEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "expToDrop", "exp-to-drop", "exp" -> instance.expToDrop = value?.coerceInt() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}