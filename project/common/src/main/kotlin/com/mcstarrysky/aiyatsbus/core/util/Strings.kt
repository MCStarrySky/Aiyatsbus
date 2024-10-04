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

import com.mcstarrysky.aiyatsbus.core.util.MathUtils.calculate
import org.bukkit.entity.Player
import taboolib.platform.compat.replacePlaceholder
import kotlin.math.roundToInt

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.StringUtils
 *
 * @author HamsterYDS
 * @since 2024/2/17 22:53
 */
fun String.replacePlaceholder(player: Player?): String {
    return if (player != null) replacePlaceholder(player) else this
}

fun String.replace(holders: List<Pair<String, Any>>, tagged: Boolean = true): String {
    var tmp = this
    holders.forEach { (holder, value) -> tmp = tmp.replace(if (tagged) "{$holder}" else holder, "$value") }
    return tmp
}

fun String.replace(holders: Map<String, Any>, tagged: Boolean = true): String = replace(holders.toList(), tagged)

fun String.replace(vararg holders: Pair<String, Any>, tagged: Boolean = true): String = replace(holders.toList(), tagged)

fun String.calculate(vararg holders: Pair<String, Any>): String {
    val result = calcToDouble(*holders)
    return if (result.isInteger()) result.toInt().toString() else result.toString()
}

fun String.calcToDouble(vararg holders: Pair<String, Any>): Double = calculate(holders.toList())

fun String.calcToInt(vararg holders: Pair<String, Any>): Int = calcToDouble(*holders).roundToInt()