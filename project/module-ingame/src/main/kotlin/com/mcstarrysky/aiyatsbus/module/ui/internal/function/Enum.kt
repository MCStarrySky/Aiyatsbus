@file:Suppress("unused")

package com.mcstarrysky.aiyatsbus.module.ui.internal.function

import com.google.common.base.Enums

inline fun <reified T : Enum<T>> String?.enumOf(transfer: (String) -> String = { it.uppercase() }): T? {
    return if (this == null) null else Enums.getIfPresent(T::class.java, transfer(this)).orNull()
}

inline fun <reified T : Enum<T>> T.next(): T = enumValues<T>().let { it[ordinal.next(it.indices)] }

inline fun <reified T : Enum<T>> T.last(): T = enumValues<T>().let { it[ordinal.last(it.indices)] }