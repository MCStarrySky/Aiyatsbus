package com.mcstarrysky.aiyatsbus.core

import com.mcstarrysky.aiyatsbus.core.data.*
import com.mcstarrysky.aiyatsbus.core.data.registry.Target
import com.mcstarrysky.aiyatsbus.core.data.registry.Rarity
import com.mcstarrysky.aiyatsbus.core.data.trigger.Trigger
import com.mcstarrysky.aiyatsbus.core.util.roman
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import taboolib.module.configuration.Configuration
import java.io.File

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
     * 附魔文件
     */
    val file: File

    /**
     * 附魔的配置
     */
    val config: Configuration

    /**
     * 附魔的基本数据
     */
    val basicData: BasicData

    /**
     * 附魔的额外数据
     */
    val alternativeData: AlternativeData

    /**
     * 附魔的依赖信息, 包括必须为 MC 哪个版本才能使用, 必须安装哪些数据包, 必须安装哪些插件
     */
    val dependencies: Dependencies

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
        return limitations.conflictsWith(other)
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
    fun displayName(level: Int? = null): String {
        return rarity.displayName(basicData.name + (level?.roman(basicData.maxLevel == 1, true) ?: ""))
    }
}