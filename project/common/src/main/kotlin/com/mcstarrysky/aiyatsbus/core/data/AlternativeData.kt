/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
    val tradeMaxLevel: Int = root?.getInt("trade_max_level", -1).coerceInt(-1),
    val enchantMaxLevel: Int = root?.getInt("enchant_max_level", -1).coerceInt(-1),
    val lootMaxLevel: Int = root?.getInt("loot_max_level", -1).coerceInt(-1),
    /** 3.0 的检测原版附魔的方法有点弱智, 把检测原版放到这里其实是更好的选择 */
    val isVanilla: Boolean = root?.getBoolean("is_vanilla", false).coerceBoolean(false)
) {

    fun getTradeLevelLimit(maxLevel: Int, globalLimit: Int): Int {
        return (if (tradeMaxLevel != -1) tradeMaxLevel else if (globalLimit != -1) globalLimit else maxLevel).coerceAtMost(maxLevel)
    }

    fun getEnchantMaxLevelLimit(maxLevel: Int, globalLimit: Int): Int {
        return (if (enchantMaxLevel != -1) enchantMaxLevel else if (globalLimit != -1) globalLimit else maxLevel).coerceAtMost(maxLevel)
    }

    fun getLootMaxLevelLimit(maxLevel: Int, globalLimit: Int): Int {
        return (if (lootMaxLevel != -1) lootMaxLevel else if (globalLimit != -1) globalLimit else maxLevel).coerceAtMost(maxLevel)
    }
}