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

import taboolib.common5.Coerce

/**
 * Vulpecular
 * top.lanscarlos.vulpecular.utils
 *
 * @author Lanscarlos
 * @since 2022-02-27 16:45
 */

fun Any?.coerceBoolean(def: Boolean? = null): Boolean {
    if (this == null && def != null) return def
    return Coerce.toBoolean(this)
}

fun Any?.coerceShort(def: Short? = null): Short {
    if (this == null && def != null) return def
    return Coerce.toShort(this)
}

fun Any?.coerceInt(def: Int? = null): Int {
    if (this == null && def != null) return def
    return Coerce.toInteger(this)
}

fun Any?.coerceLong(def: Long? = null): Long {
    if (this == null && def != null) return def
    return Coerce.toLong(this)
}

fun Any?.coerceFloat(def: Float? = null): Float {
    if (this == null && def != null) return def
    return Coerce.toFloat(this)
}

fun Any?.coerceDouble(def: Double? = null): Double {
    if (this == null && def != null) return def
    return Coerce.toDouble(this)
}

inline fun <reified T> Any?.coerceList(def: List<T> = emptyList(), transfer: (Any?) -> T): List<T> {
    return when (this) {
        is T -> listOf(this)
        is Array<*> -> this.map(transfer)
        is Collection<*> -> this.map(transfer)
        else -> def
    }
}

inline fun <reified T: Any> Any?.coerceListNotNull(def: List<T> = emptyList(), transfer: (Any?) -> T?): List<T> {
    return when (this) {
        is T -> listOf(this)
        is Array<*> -> this.mapNotNull(transfer)
        is Collection<*> -> this.mapNotNull(transfer)
        else -> def
    }
}