package com.mcstarrysky.aiyatsbus.core.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import taboolib.common.util.unsafeLazy
import taboolib.module.chat.Components
import taboolib.module.chat.Source

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.AdventureUtils
 *
 * @author mical
 * @since 2024/2/18 00:39
 */
object AdventureUtils {

    private val gson: GsonComponentSerializer by unsafeLazy {
        GsonComponentSerializer.gson()
    }

    fun toTabooLibComponent(component: Any): Source {
        component as Component
        return Components.parseRaw(gson.serialize(component))
    }

    fun fromTabooLibComponent(component: Source): Any {
        return gson.deserialize(component.toRawMessage())
    }
}