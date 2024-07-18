package com.mcstarrysky.aiyatsbus.core

import org.bukkit.NamespacedKey
import java.io.File

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentManager
 *
 * @author mical
 * @since 2024/2/17 16:16
 */
interface AiyatsbusEnchantmentManager {

    /**
     * 获取附魔
     */
    fun getEnchant(key: NamespacedKey): AiyatsbusEnchantment?

    /**
     * 获取附魔
     */
    fun getEnchant(key: String): AiyatsbusEnchantment?

    /**
     * 根据名称获取附魔
     */
    fun getByName(name: String): AiyatsbusEnchantment?

    /**
     * 获取全部附魔
     */
    fun getEnchants(): Map<NamespacedKey, AiyatsbusEnchantment>

    /**
     * 注册附魔
     */
    fun register(enchantment: AiyatsbusEnchantmentBase)

    /**
     * 取消注册附魔
     */
    fun unregister(enchantment: AiyatsbusEnchantment)

    /**
     * 加载附魔
     */
    fun loadEnchantments()

    /**
     * 从文件中加载附魔
     */
    fun loadFromFile(file: File)

    /**
     * 删除附魔
     */
    fun clearEnchantments()
}