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
     * 获取事件触发器
     */
    fun getEventExecutor(): AiyatsbusEventExecutor

    /**
     * 获取 Kether 处理接口
     */
    fun getKetherHandler(): AiyatsbusKetherHandler

    /**
     * 获取 Aiyatsbus 中的语言文件接口
     */
    fun getLanguage(): AiyatsbusLanguage

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

    /**
     * 获取附魔调度器
     */
    fun getTickHandler(): AiyatsbusTickHandler
}