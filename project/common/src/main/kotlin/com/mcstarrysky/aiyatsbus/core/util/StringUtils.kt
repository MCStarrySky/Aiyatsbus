package com.mcstarrysky.aiyatsbus.core.util

import com.mcstarrysky.aiyatsbus.core.util.MathUtils.calculate
import kotlin.math.roundToInt

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.StringUtils
 *
 * @author HamsterYDS
 * @since 2024/2/17 22:53
 */
fun String.replace(holders: List<Pair<String, Any>>, tagged: Boolean = true): String {
    var tmp = this
    holders.forEach { (holder, value) -> tmp = tmp.replace(if (tagged) "{$holder}" else holder, "$value") }
    return tmp
}

fun String.replace(holders: Map<String, Any>, tagged: Boolean = true): String = replace(holders.toList(), tagged)

fun String.replace(vararg holders: Pair<String, Any>, tagged: Boolean = true): String = replace(holders.toList(), tagged)

fun String.calculate(vararg holders: Pair<String, Any>): String = calcToDouble(*holders).toString()
fun String.calcToDouble(vararg holders: Pair<String, Any>): Double = calculate(holders.toList())
fun String.calcToInt(vararg holders: Pair<String, Any>): Int = calcToDouble(*holders).roundToInt()