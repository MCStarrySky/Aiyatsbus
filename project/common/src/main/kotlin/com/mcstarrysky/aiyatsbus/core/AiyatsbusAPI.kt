package com.mcstarrysky.aiyatsbus.core

import com.mcstarrysky.aiyatsbus.core.registration.AiyatsbusEnchantmentRegisterer

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusAPI
 *
 * @author mical
 * @since 2024/2/17 15:31
 */
interface AiyatsbusAPI {

    /**
     * 获取附魔筛选接口
     */
    fun getEnchantmentFilter(): AiyatsbusEnchantmentFilter

    /**
     * 获取附魔管理接口
     */
    fun getEnchantmentManager(): AiyatsbusEnchantmentManager

    /**
     * 获取附魔注册接口
     */
    fun getEnchantmentRegisterer(): AiyatsbusEnchantmentRegisterer

    /**
     * 获取 Kether 处理接口
     */
    fun getKetherHandler(): AiyatsbusKetherHandler

    /**
     * 获取附魔展示接口
     */
    fun getDisplayManager(): AiyatsbusDisplayManager

    /**
     * 获取 Aiyatsbus 中的 NMS 接口
     */
    fun getMinecraftAPI(): AiyatsbusMinecraftAPI

    /**
     * 获取玩家数据接口
     */
    fun getPlayerDataHandler(): AiyatsbusPlayerDataHandler
}