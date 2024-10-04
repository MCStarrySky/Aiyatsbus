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