package com.mcstarrysky.aiyatsbus.core.data

import taboolib.library.configuration.ConfigurationSection

/**
 * 附魔的基本数据, 必填的
 *
 * @author mical
 * @since 2024/2/17 14:37
 */
data class BasicData(
    private val root: ConfigurationSection,
    val enable: Boolean = root.getBoolean("enable", true),
    val disableWorlds: List<String> = root.getStringList("disable_worlds"),
    val id: String = root.getString("id")!!,
    val name: String = root.getString("name")!!,
    val nameDisplay: String = root.getString("name_display", name)!!,
    val maxLevel: Int = root.getInt("max_level", 1)
)