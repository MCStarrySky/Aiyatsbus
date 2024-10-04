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
package com.mcstarrysky.aiyatsbus.module.kether.operation.operation.charge

import com.mcstarrysky.aiyatsbus.core.event.AiyatsbusBowChargeEvent
import com.mcstarrysky.aiyatsbus.core.util.coerceDouble
import com.mcstarrysky.aiyatsbus.core.util.realDamage
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
import taboolib.library.xseries.XMaterial
import taboolib.platform.util.checkItem

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.operation.operation.charge.Charge
 *
 * @author mical
 * @since 2024/9/19 22:07
 */
object Charge {

    private val arrows = listOfNotNull(
        XMaterial.ARROW.parseItem(),
        XMaterial.SPECTRAL_ARROW.parseItem(),
        XMaterial.TIPPED_ARROW.parseItem()
    )

    @Suppress("UNCHECKED_CAST")
    fun charge(args: List<Any?>?) {
        args ?: return
        charge(
            args[0] as? Player ?: return,
            args[1].coerceDouble(),
            args[2] as? AiyatsbusBowChargeEvent.Prepare ?: return
        )
    }

    fun charge(player: Player, health: Double, event: AiyatsbusBowChargeEvent.Prepare) {
        if (arrows.any { player.inventory.checkItem(it) }) {
            return
        }
        if (player.health >= health) {
            submit {
                player.realDamage(health)
            }
            event.isAllowed = true
        }
    }
}