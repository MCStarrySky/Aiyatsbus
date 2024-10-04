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
package com.mcstarrysky.aiyatsbus.core.compat

import org.bukkit.inventory.ItemStack
import java.util.LinkedList

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.compat.QuickShopGuardChecker
 *
 * @author mical
 * @since 2024/8/19 09:41
 */
interface GuardItemChecker {

    fun checkIsGuardItem(itemStack: ItemStack): Boolean

    companion object {

        val registeredIntegrations = LinkedList<GuardItemChecker>()

        /**
         * 检查是否为受保护的物品
         * 原本为 QuickShop 的两个分支而设计, 现在任何插件都可以来做适配
         */
        fun checkIsGuardItem(itemStack: ItemStack): Boolean {
            return registeredIntegrations.isNotEmpty() &&
                    registeredIntegrations.all { it.checkIsGuardItem(itemStack) }
        }
    }
}