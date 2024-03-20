package com.mcstarrysky.aiyatsbus.core

import com.google.common.collect.Table

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusTickHandler
 *
 * @author mical
 * @since 2024/3/20 21:14
 */
interface AiyatsbusTickHandler {

    /**
     * 获取所有附魔调度器
     * AiyatsbusEnchantment -> 附魔对象
     * String -> 调度器 ID
     * Long -> 循环时间间隔
     */
    fun getRoutine(): Table<AiyatsbusEnchantment, String, Long>

    /**
     * 重置所有附魔调度器
     */
    fun reset()

    /**
     * 开始运行
     */
    fun start()
}