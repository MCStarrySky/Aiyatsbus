package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.`object`

import com.mcstarrysky.aiyatsbus.core.util.parentheses
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.ObjectEntry
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objString

object ObjectString : ObjectEntry<String>() {

    override fun get(from: String, objName: String): Pair<ObjectEntry<*>, Any?> {
        return when {
            objName.startsWith("分割包含") -> {
                val parts = objName.parentheses.first().split(";")
                println(parts)
                return objString to from.split(parts[0]).contains(parts[1])
            }
            else -> super.get(from, objName)
        }
    }

    override fun holderize(obj: String) = this to obj
    override fun disholderize(holder: String) = holder
}