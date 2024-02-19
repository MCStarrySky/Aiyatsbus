package com.mcstarrysky.aiyatsbus.core

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentManager
 *
 * @author mical
 * @since 2024/2/17 16:16
 */
interface AiyatsbusEnchantmentManager {

    /**
     * 获取根据 ID 的所有附魔
     */
    fun getByIDs(): Map<String, AiyatsbusEnchantment>

    /**
     * 根据 ID 获取附魔
     */
    fun getByID(id: String): AiyatsbusEnchantment?

    /**
     * 获取根据名称的所有附魔
     */
    fun getByNames(): Map<String, AiyatsbusEnchantment>

    /**
     * 根据名称获取附魔
     */
    fun getByName(name: String): AiyatsbusEnchantment?

    /**
     * 注册附魔
     */
    fun register(enchantment: AiyatsbusEnchantmentBase)

    /**
     * 加载附魔
     */
    fun loadEnchantments()

    /**
     * 删除附魔
     */
    fun clearEnchantments()
}