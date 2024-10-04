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

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.player.PlayerItemBreakEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player.PropertyPlayerItemBreakEvent
 *
 * @author yanshiqwq
 * @since 2024/4/2 00:34
 */
@AiyatsbusProperty(
    id = "player-item-break-event",
    bind = PlayerItemBreakEvent::class
)
class PropertyPlayerItemBreakEvent : AiyatsbusGenericProperty<PlayerItemBreakEvent>("player-item-break-event") {

    override fun readProperty(instance: PlayerItemBreakEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "brokenItem", "item" -> instance.brokenItem
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: PlayerItemBreakEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}