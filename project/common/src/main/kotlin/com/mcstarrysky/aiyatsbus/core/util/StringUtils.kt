package com.mcstarrysky.aiyatsbus.core.util

import taboolib.common5.cbool
import taboolib.common5.cdouble
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

fun String.calculate(holders: List<Pair<String, Any>>): String {
    return replace(holders).run {
        try {
            MathUtils.calculate(this).toString()
        } catch (ex: Throwable) {
            throw RuntimeException("Failed to calculate $this", ex)
        }
    }
}

fun String.calculate(holders: Map<String, Any>): String = calculate(holders.toList())
fun String.calcToDouble(holders: Map<String, Any>): Double = calculate(holders).cdouble
fun String.calcToBoolean(holders: Map<String, Any>): Boolean = calculate(holders).cbool
fun String.calcToInt(holders: Map<String, Any>): Int = calcToDouble(holders).roundToInt()
fun String.calculate(vararg holders: Pair<String, Any>): String = calculate(holders.toList())
fun String.calcToDouble(vararg holders: Pair<String, Any>): Double = calculate(*holders).cdouble
fun String.calcToBoolean(vararg holders: Pair<String, Any>): Boolean = calculate(*holders).cbool
fun String.calcToInt(vararg holders: Pair<String, Any>): Int = calcToDouble(*holders).roundToInt()