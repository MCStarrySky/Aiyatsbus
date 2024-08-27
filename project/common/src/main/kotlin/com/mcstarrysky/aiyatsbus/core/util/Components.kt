package com.mcstarrysky.aiyatsbus.core.util

import com.google.gson.JsonParser
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import taboolib.module.chat.ComponentText
import taboolib.module.chat.component

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.registration.modern.Utils
 *
 * @author mical
 * @since 2024/2/17 17:07
 */
/**
 * 旧版 JsonParser
 * 旧版没有 parseString 静态方法
 */
val JSON_PARSER = JsonParser()

/**
 * 旧版文本序列化器
 */
val LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
    .character('\u00a7')
    .useUnusualXRepeatedCharacterHexFormat()
    .hexColors()
    .build()

/**
 * 将带有 &a 之类的旧版文本转换为 Adventure Component
 */
fun String.legacyToAdventure(): Component {
    return LEGACY_COMPONENT_SERIALIZER.deserialize(this)
}

/**
 * 将 List<String> 构建为复合文本并上色
 */
fun List<String>?.toBuiltComponent(): List<ComponentText> {
    return this?.map { it.component().buildColored() } ?: emptyList()
}