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
package com.mcstarrysky.aiyatsbus.core.util

import com.mcstarrysky.aiyatsbus.core.cooldown
import org.bukkit.entity.Player
import taboolib.common5.format

/**
 * 记录冷却
 */
fun Player.addCd(key: String) {
    cooldown[key] = System.currentTimeMillis()
}

/**
 * 移除玩家某个冷却
 */
fun Player.removeCd(key: String) {
    cooldown.remove(key)
}

/**
 * 清除玩家所有冷却
 */
fun Player.clearCd() {
    cooldown.clear()
}

// pair#first 冷却是否结束，冷却中为false
// pair#second 冷却若未结束，离结束还剩下的时间（秒）
fun Player.checkCd(key: String, cd: Double): Pair<Boolean, Double> {
    if (!cooldown.containsKey(key))
        return true to 0.0
    val tmp = (cd - (System.currentTimeMillis() - cooldown[key]!!) / 1000.0).format(1)
    return if (tmp <= 0.0) true to -1.0 else false to maxOf(tmp, 0.0)
}