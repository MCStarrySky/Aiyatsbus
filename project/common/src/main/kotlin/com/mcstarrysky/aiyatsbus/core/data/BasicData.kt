package com.mcstarrysky.aiyatsbus.core.data

import taboolib.common5.cint
import taboolib.library.configuration.ConfigurationSection

/**
 * 附魔的基本数据, 必填的
 *
 * @author mical
 * @since 2024/2/17 14:37
 */
data class BasicData(
    val enable: Boolean,
    val disableWorlds: List<String>,
    val id: String,
    var name: String,
    var maxLevel: Int
) {

    companion object {

        fun load(conf: ConfigurationSection): BasicData {
            return BasicData(conf.getBoolean("enable", true), conf.getStringList("disable_worlds"),
                conf["id"].toString(), conf["name"].toString(), conf["max_level"].cint
            )
        }
    }
}