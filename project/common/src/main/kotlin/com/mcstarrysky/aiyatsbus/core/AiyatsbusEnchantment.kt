package com.mcstarrysky.aiyatsbus.core

import com.mcstarrysky.aiyatsbus.core.data.*
import com.mcstarrysky.aiyatsbus.core.data.Target
import com.mcstarrysky.aiyatsbus.core.trigger.Trigger
import com.mcstarrysky.aiyatsbus.core.util.roman
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.chat.colored

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
 *
 * @author mical
 * @since 2024/2/17 14:04
 */
interface AiyatsbusEnchantment {

    /**
     * 附魔标识
     */
    val id: String

    /**
     * 附魔的 Key
     */
    val enchantmentKey: NamespacedKey

    /**
     * 附魔的基本数据
     */
    val basicData: BasicData

    /**
     * 附魔的额外数据
     */
    val alternativeData: AlternativeData

    /**
     * Bukkit 附魔实例, 在注册后赋值, 一般是 CraftEnchantment
     *
     * 在 1.20.2 及以下版本中, 这个是 LegacyCraftEnchantment
     * 在 1.20.2 以上版本中, Bukkit 更改了注册附魔的方式, 这个一般是 AiyatsbusCraftEnchantment
     */
    val enchantment: Enchantment

    /**
     * 附魔品质
     */
    val rarity: Rarity

    /**
     * 附魔的变量显示与替换
     */
    val variables: Variables

    /**
     * 附魔显示
     */
    val displayer: Displayer

    /**
     * 附魔对象
     */
    val targets: List<Target>

    /**
     * 附魔限制
     */
    val limitations: Limitations

    /**
     * 附魔的触发器
     */
    val trigger: Trigger

    fun conflictsWith(other: Enchantment): Boolean {
        return false
    }

    /**
     * 支持了粘液的附魔机
     */
    fun canEnchantItem(item: ItemStack): Boolean {
        return limitations.checkAvailable(CheckType.ANVIL, item).first
    }

    /**
     * 显示名称
     */
    fun displayName(level: Int? = null, sender: CommandSender = Bukkit.getConsoleSender()): String {
        return rarity.displayName(sender.parseLang(basicData.name) + (level?.roman(basicData.maxLevel == 1, true) ?: ""))
    }
}