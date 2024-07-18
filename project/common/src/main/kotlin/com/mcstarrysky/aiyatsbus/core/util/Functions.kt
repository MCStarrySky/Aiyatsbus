package com.mcstarrysky.aiyatsbus.core.util

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.Function3
 *
 * @author mical
 * @since 2024/7/18 16:55
 */
fun interface Function1<in T> {

    fun apply(t: T)
}

fun interface Function2<in T, R> {

    fun apply(t: T): R
}

fun interface Function3<in T, in R, C> {

    fun apply(t: T, r: R): C
}

fun interface Function4<in T, in R, in C, K> {

    fun apply(t: T, r: R, c: C): K
}