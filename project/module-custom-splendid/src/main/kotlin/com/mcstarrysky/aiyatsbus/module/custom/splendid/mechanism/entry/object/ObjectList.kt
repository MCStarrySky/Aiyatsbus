package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.`object`

import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.*

object ObjectList : ObjectEntry<Pair<ObjectEntry<*>, MutableList<String>>>() {

    override fun modify(obj: Pair<ObjectEntry<*>, MutableList<String>>, cmd: String, params: List<String>): Boolean {
        return when (cmd) {
            "添加元素" -> {
                return obj.second.add(params[0])
            }
            "移除元素" -> {
                return obj.second.remove(params[0])
            }
            "清空元素" -> obj.second.clear().let { true }
            else -> false
        }
    }

    override fun get(from: Pair<ObjectEntry<*>, MutableList<String>>, objName: String): Pair<ObjectEntry<*>, Any?> {
        return when {
            objName.startsWith("包含元素") -> {
                val element = objName.removePrefix("包含元素(").removeSuffix(")")
                return objString.h(from.second.contains(element))
            }
            objName == "大小" -> objString.h(from.second.size)
            else -> objString.h(null)
        }
    }

    override fun holderize(obj: Pair<ObjectEntry<*>, MutableList<String>>) =
        this to registeredRegistries.inverse()[obj.first] + ":[" + obj.second.joinToString(";") + "]"

    override fun disholderize(holder: String): Pair<ObjectEntry<*>, MutableList<String>> {
        val parts = holder.split(":")
        val objEntry = registeredRegistries[parts[0]] ?: objString
        val content = parts[1].replace("[", "").replace("]", "")
        return objEntry to if (content.isBlank()) mutableListOf() else content.split(";").toMutableList()
    }
}