@file:Suppress("unused")

package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function

import java.math.RoundingMode

fun Int.next(range: IntRange): Int {
    var next = this + 1
    if (next > range.last) {
        next = range.first
    }
    return next
}

fun Int.last(range: IntRange): Int {
    var last = this - 1
    if (last < range.first) {
        last = range.last
    }
    return last
}

fun Double.round(scale: Int = 2): Double = toBigDecimal().setScale(scale, RoundingMode.HALF_DOWN).toDouble()