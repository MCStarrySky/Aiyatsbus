package com.mcstarrysky.aiyatsbus.core.data

import com.mcstarrysky.aiyatsbus.core.AiyatsbusDisplayManager
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.util.replace
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
    /** 附魔显示的前半部分, 一般是名称和等级 */
    val previous: String,
    /** 附魔显示的后半部分, 一般是描述并且换行写 */
    val subsequent: String,
    /** 描述 */
    val generalDescription: String,
    /** 一般有变量的描述会用这个替换变量, 这个不写默认为普通描述 */
    val specificDescription: String,
    /** 附魔 */
    val enchant: AiyatsbusEnchantment
) {

    /**
     * 是否是在 display.yml 中设置的默认配置格式
     */
    fun isDefaultDisplay() = previous == "{default_previous}" && subsequent == "{default_subsequent}"

    /**
     * 生成本附魔在当前状态下的显示, 在非合并模式下
     */
    fun display(level: Int?, player: Player?, item: ItemStack?) = display(holders(level, player, item))

    /**
     * 生成本附魔在当前状态下的显示, 在非合并模式下
     */
    fun display(holders: Map<String, String>): String {
        return (previous.replace("{default_previous}", AiyatsbusDisplayManager.defaultPrevious)
                + subsequent.replace("{default_subsequent}", AiyatsbusDisplayManager.defaultSubsequent)
                ).replace(holders).colored()
    }

    /**
     * 生成本附魔在当前状态下的显示, 在合并模式下
     */
    fun displays(
        level: Int? = null,
        player: Player? = null,
        item: ItemStack? = null,
        index: Int? = null
    ): Map<String, String> {
        val suffix = index?.let { "_$it" } ?: ""
        val holders = holders(level, player, item)
        return mapOf(
            "previous$suffix" to previous.replace("{default_previous}", AiyatsbusDisplayManager.defaultPrevious).replace(holders).colored(),
            "subsequent$suffix" to subsequent.replace("{default_subsequent}", AiyatsbusDisplayManager.defaultSubsequent).replace(holders).colored()
        )
    }

    /**
     * 生成本附魔在当前状态下的变量替换 Map
     */
    fun holders(
        level: Int? = null,
        player: Player? = null,
        item: ItemStack? = null
    ): Map<String, String> {
        val tmp = enchant.variables.variables(level, player, item, true).toMutableMap()
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
        tmp["enchant_display_lore"] = display(tmp)
        tmp["description"] = specificDescription.replace(tmp).colored()
        return tmp
    }

    companion object {

        fun load(displayerConfig: ConfigurationSection, enchant: AiyatsbusEnchantment): Displayer {
            val previous = displayerConfig.getString("format.previous", "{default_previous}")!!
            val subsequent = displayerConfig.getString("format.subsequent", "{default_subsequent}")!!
            val generalDescription = displayerConfig.getString("description.general", "§7附魔介绍未设置")!!
            val specificDescription = displayerConfig.getString("description.specific", generalDescription)!!
            return Displayer(previous, subsequent, generalDescription, specificDescription, enchant)
        }
    }
}