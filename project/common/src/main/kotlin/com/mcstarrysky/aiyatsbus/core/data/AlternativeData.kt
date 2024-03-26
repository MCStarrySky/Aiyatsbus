package com.mcstarrysky.aiyatsbus.core.data

import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import taboolib.library.configuration.ConfigurationSection

/**
 * 附魔的额外数据, 一般不需要填写
 *
 * @author mical
 * @since 2024/2/17 15:11
 */
data class AlternativeData(
    private val root: ConfigurationSection?,
    val grindstoneable: Boolean = root?.getBoolean("grindstoneable", true).coerceBoolean(true),
    val weight: Int = root?.getInt("weight", 100).coerceInt(100),
    val isTreasure: Boolean = root?.getBoolean("is_treasure", false).coerceBoolean(false),
    val isCursed: Boolean = root?.getBoolean("is_cursed", false).coerceBoolean(false),
    val isTradeable: Boolean = root?.getBoolean("is_tradeable", true).coerceBoolean(true),
    val isDiscoverable: Boolean = root?.getBoolean("is_discoverable", true).coerceBoolean(true),
    /** 3.0 的检测原版附魔的方法有点弱智, 把检测原版放到这里其实是更好的选择 */
    val isVanilla: Boolean = root?.getBoolean("is_vanilla", false).coerceBoolean(false)
)