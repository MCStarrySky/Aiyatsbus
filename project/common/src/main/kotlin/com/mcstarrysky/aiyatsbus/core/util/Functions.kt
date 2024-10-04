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