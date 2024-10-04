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
package com.mcstarrysky.aiyatsbus.module.kether.action.game

import com.mcstarrysky.aiyatsbus.core.compat.AntiGriefChecker
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusParser
import com.mcstarrysky.aiyatsbus.module.kether.aiyatsbus
import taboolib.platform.util.toBukkitLocation

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionAntiGriefChecker
 *
 * @author mical
 * @since 2024/3/23 18:08
 */
object ActionAntiGriefChecker {

    @AiyatsbusParser(["anti-grief-checker", "anti-grief", "agc"])
    fun antiGriefChecker() = aiyatsbus {
        when (nextToken()) {
            "can-break" -> {
                combine(player(), location()) { p, l ->
                    AntiGriefChecker.canBreak(p, l.toBukkitLocation())
                }
            }
            "can-damage" -> {
                combine(player(), entity()) { p, e ->
                    AntiGriefChecker.canDamage(p, e)
                }
            }
            else -> error("unknown operation")
        }
    }
}