package com.mcstarrysky.aiyatsbus.core.data

import taboolib.library.configuration.ConfigurationSection

/**
 * 附魔的额外数据, 一般不需要填写
 *
 * @author mical
 * @since 2024/2/17 15:11
 */
class AlternativeData(
    section: ConfigurationSection?
) {

    var grindstoneable: Boolean = true
    var weight: Int = 100
    var isTreasure: Boolean = false
    var isCursed: Boolean = false
    var isTradeable: Boolean = true
    var isDiscoverable: Boolean = true

    /** 3.0 的检测原版附魔的方法有点弱智, 把检测原版放到这里其实是更好的选择 */
    var isVanilla: Boolean = false

    init {
        section?.run {
            grindstoneable = getBoolean("grindstoneable", true)
            weight = getInt("weight", 100)
            isTreasure = getBoolean("is_treasure", false)
            isCursed = getBoolean("is_cursed", false)
            isTradeable = getBoolean("is_tradeable", true)
            isDiscoverable = getBoolean("is_discoverable", true)
            isVanilla = getBoolean("is_vanilla", false)
        }
    }
}