package com.mcstarrysky.aiyatsbus.core

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.configuration.Configuration

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusDisplayManager
 *
 * @author mical
 * @since 2024/2/17 21:59
 */
interface AiyatsbusDisplayManager {

    /**
     * 获取显示模块配置
     */
    fun getSettings(): Settings

    /**
     * 按照配置中设定的品质排行顺序整理附魔
     */
    fun sortEnchants(enchants: Map<AiyatsbusEnchantment, Int>): LinkedHashMap<AiyatsbusEnchantment, Int>

    /**
     * 展示附魔, 展示是给玩家看的, 玩家必须存在
     */
    fun display(item: ItemStack, player: Player): ItemStack

    /**
     * 取消展示
     */
    fun undisplay(item: ItemStack, player: Player): ItemStack

    interface Settings {

        var conf: Configuration

        /** 是否启用内置附魔显示 */
        var enable: Boolean

        /** 默认附魔前缀 */
        var defaultPrevious: String

        /** 默认附魔后缀 */
        var defaultSubsequent: String

        /** 附魔词条数空余 */
        var capabilityLine: String

        /** 按等级排序, 由高到低 */
        var sortByLevel: Boolean

        /** 品质顺序 */
        val rarityOrder: List<String>

        /** 开启合并显示 */
        var combine: Boolean

        /** 当物品附魔超过多少时开启合并显示 */
        var combineMinimal: Int

        /** 每次合并几个附魔, 也就是每行显示几个 */
        var combineAmount: Int

        /** 合并布局 */
        var combineLayout: List<String>

        /** Lore 显示顺序 (物品本身有 Lore 时) */
        var hasLoreFormation: List<String>

        /** Lore 显示顺序 (物品本身没有 Lore 时) */
        var withoutLoreFormation: List<String>

        /** 是否独立在最后单行显示拥有特殊显示的附魔 */
        var separateSpecial: Boolean
    }
}