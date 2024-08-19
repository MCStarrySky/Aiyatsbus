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