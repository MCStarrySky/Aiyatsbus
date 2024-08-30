package com.mcstarrysky.aiyatsbus.core.util

import kotlin.Pair

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

fun interface Function3To2<in T, in R, in C, B, K> {

    /**
     * FIXME: 暂时没考虑 java 兼容性
     */
    fun apply(t: T, r: R, c: C): Pair<B, K>
}

fun interface Function2To2<in T, in R, B, K> {

    /**
     * FIXME: 暂时没考虑 java 兼容性
     */
    fun apply(t: T, r: R): Pair<B, K>
}