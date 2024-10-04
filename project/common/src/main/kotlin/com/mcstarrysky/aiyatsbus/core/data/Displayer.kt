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

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.util.replace
import com.mcstarrysky.aiyatsbus.core.util.replacePlaceholder
import com.mcstarrysky.aiyatsbus.core.util.roman
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.chat.colored

/**
 * 附魔显示
 *
 * @author mical
 * @since 2024/2/17 22:23
 */
data class Displayer(
    private val root: ConfigurationSection,
    private val enchant: AiyatsbusEnchantment,
    /** 附魔显示的前半部分, 一般是名称和等级 */
    val previous: String = root.getString("format.previous", "{default_previous}")!!,
    /** 附魔显示的后半部分, 一般是描述并且换行写 */
    val subsequent: String = root.getString("format.subsequent", "{default_subsequent}")!!,
    /** 描述 */
    val generalDescription: String = root.getString("description.general", "&7")!!,
    /** 一般有变量的描述会用这个替换变量, 这个不写默认为普通描述 */
    val specificDescription: String = root.getString("description.specific", generalDescription)!!
) {

    private val displayManagerSettings = Aiyatsbus.api().getDisplayManager().getSettings()

    /**
     * 是否是在 display.yml 中设置的默认配置格式
     */
    fun isDefaultDisplay() = previous == "{default_previous}" && subsequent == "{default_subsequent}"

    /**
     * 生成本附魔在当前状态下的显示, 在非合并模式下
     */
    fun display(level: Int, player: Player?, item: ItemStack?) = display(holders(level, player, item))

    /**
     * 生成本附魔在当前状态下的显示, 在非合并模式下
     */
    fun display(holders: Map<String, String>): String {
        return (previous.replace("{default_previous}", displayManagerSettings.defaultPrevious)
                + subsequent.replace("{default_subsequent}", displayManagerSettings.defaultSubsequent)
                ).replace(holders).colored()
    }

    /**
     * 生成本附魔在当前状态下的显示, 在合并模式下
     */
    fun displays(
        level: Int,
        player: Player? = null,
        item: ItemStack? = null,
        index: Int? = null
    ): Map<String, String> {
        val suffix = index?.let { "_$it" } ?: ""
        val holders = holders(level, player, item)
        return mapOf(
            "previous$suffix" to previous.replace("{default_previous}", displayManagerSettings.defaultPrevious)
                .replace(holders).colored(),
            "subsequent$suffix" to subsequent.replace("{default_subsequent}", displayManagerSettings.defaultSubsequent)
                .replace(holders).colored()
        )
    }

    /**
     * 生成本附魔在当前状态下的变量替换 Map
     */
    fun holders(
        level: Int,
        player: Player? = null,
        item: ItemStack? = null
    ): Map<String, String> {
        val tmp = enchant.variables.variables(level, item, true)
            .mapValues { it.value.toString() }.toMutableMap() // 因为是显示, 这里的变量可以直接转为字符串
        val lv = level ?: enchant.basicData.maxLevel
        tmp["id"] = enchant.basicData.id
        tmp["name"] = enchant.basicData.name
        tmp["level"] = "$lv"
        tmp["roman_level"] = lv.roman(enchant.basicData.maxLevel == 1)
        tmp["roman_level_with_a_blank"] = lv.roman(enchant.basicData.maxLevel == 1, true)
        tmp["max_level"] = "${enchant.basicData.maxLevel}"
        tmp["rarity"] = enchant.rarity.name
        tmp["rarity_display"] = enchant.rarity.displayName()
        tmp["enchant_display"] = enchant.displayName()
        tmp["enchant_display_roman"] = enchant.displayName(lv)
        tmp["enchant_display_lore"] = display(tmp).replacePlaceholder(player)
        tmp["description"] = specificDescription.replace(tmp).colored().replacePlaceholder(player)
        return tmp
    }
}