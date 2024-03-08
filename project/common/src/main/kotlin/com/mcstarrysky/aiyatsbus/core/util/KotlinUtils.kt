package com.mcstarrysky.aiyatsbus.core.util

import taboolib.common5.cdouble

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.KotlinUtils
 *
 * @author mical
 * @since 2024/2/18 12:32
 */
@Suppress("UNUSED_PARAMETER")
operator fun <T> List<T>.get(index: Int, fuckKotlin: Int) = getOrNull(index)

fun <T> List<T>.subList(index: Int) = if (size > index) subList(index, size) else emptyList()

operator fun <T> List<T>.get(index: Int, default: T) = getOrElse(index) { default }

val number = "(-?\\d+)(\\.\\d+)?".toRegex()

val String.numbers get() = number.findAll(this).toList().map { it.value.cdouble }