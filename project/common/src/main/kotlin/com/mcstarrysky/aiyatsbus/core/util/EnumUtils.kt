package com.mcstarrysky.aiyatsbus.core.util

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.EnumUtils
 *
 * @author mical
 * @since 2024/7/18 00:33
 */
import com.google.common.base.Enums

inline fun <reified T : Enum<T>> String?.enumOf(transfer: (String) -> String = { it.uppercase() }): T? {
    return if (this == null) null else Enums.getIfPresent(T::class.java, transfer(this)).orNull()
}