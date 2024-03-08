package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.`object`

import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.*

object ObjectList : ObjectEntry<Pair<ObjectEntry<*>, List<String>>>() {

    override fun holderize(obj: Pair<ObjectEntry<*>, List<String>>) =
        this to registeredRegistries.inverse()[obj.first] + ":[" + obj.second.joinToString(",") + "]"

    override fun disholderize(holder: String): Pair<ObjectEntry<*>, List<String>> {
        val parts = holder.split(":")
        val objEntry = registeredRegistries[parts[0]] ?: objString
        return objEntry to parts[1].replace("[", "").replace("]", "").split(",")
    }
}