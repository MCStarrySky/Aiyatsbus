package com.mcstarrysky.aiyatsbus.core.util

import com.google.common.base.Enums
import com.google.gson.Gson
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.library.reflex.ReflexClass
import taboolib.library.reflex.UnsafeAccess
import java.lang.reflect.Field

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.registration.modern.Utils
 *
 * @author mical
 * @since 2024/2/17 17:07
 */
val GSON = Gson()

inline fun <reified T : Enum<T>> String?.enumOf(transfer: (String) -> String = { it.uppercase() }): T? {
    return if (this == null) null else Enums.getIfPresent(T::class.java, transfer(this)).orNull()
}

@Suppress("UNCHECKED_CAST")
fun <T> Any?.invokeMethodDeep(name: String): T? {
    var result: Any? = this
    for (method in name.split('/')) {
        result = result?.invokeMethod(method)
    }
    return result as? T
}

fun Class<*>.assignableFrom(clazz: ReflexClass): Boolean {
    return clazz.structure.interfaces.any { it.name == name }
}

fun String.isValidJson(): Boolean {
    if (trim().isEmpty() || !startsWith("{")) return false
    return kotlin.runCatching {
        GSON.fromJson(this, Any::class.java)
    }.isSuccess
}

/**
 * 设置 static final 字段
 */
fun Field.setStaticFinal(value: Any) {
    val offset = UnsafeAccess.unsafe.staticFieldOffset(this)
    UnsafeAccess.unsafe.putObject(UnsafeAccess.unsafe.staticFieldBase(this), offset, value)
}

/**
 * ItemsAdder 是否存在
 */
internal val itemsAdderEnabled = runCatching { Class.forName("dev.lone.itemsadder.api.ItemsAdder") }.isSuccess