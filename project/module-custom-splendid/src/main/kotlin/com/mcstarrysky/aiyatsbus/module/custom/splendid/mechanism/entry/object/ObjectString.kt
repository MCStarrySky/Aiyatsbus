package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.`object`

import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.ObjectEntry

object ObjectString : ObjectEntry<String>() {

    override fun modify(obj: String, cmd: String, params: List<String>): Boolean {
        return when (cmd) {
            "分割包含" -> {
                val split = params[0]
                val contain = params[1]
                obj.split(split).contains(contain)
            }

            else -> true
        }
    }

    override fun get(from: String, objName: String): Pair<ObjectEntry<*>, Any?> {
        return super.get(from, objName)
    }

    override fun holderize(obj: String) = this to obj
    override fun disholderize(holder: String) = holder
}