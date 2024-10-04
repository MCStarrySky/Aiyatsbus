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
package com.mcstarrysky.aiyatsbus.module.compat.item

import com.mcstarrysky.aiyatsbus.core.compat.GuardItemChecker
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.compat.QuickShopGruardCheckerImpl
 *
 * @author mical
 * @since 2024/8/19 09:50
 */
object QuickShopGuardCheckerHikariImpl : GuardItemChecker {

    override fun checkIsGuardItem(itemStack: ItemStack): Boolean {
        return AbstractDisplayItemHikari.checkIsGuardItemStack(itemStack)
    }

    @Awake(LifeCycle.ACTIVE)
    fun init() {
        if (kotlin.runCatching {
                Class.forName("com.ghostchu.quickshop.shop.display.AbstractDisplayItem")
            }.isSuccess) {
            GuardItemChecker.registeredIntegrations += this
        }
    }
}

object QuickShopGuardCheckerReremakeImpl : GuardItemChecker {

    override fun checkIsGuardItem(itemStack: ItemStack): Boolean {
        return AbstractDisplayItemReremake.checkIsGuardItemStack(itemStack)
    }

    @Awake(LifeCycle.ACTIVE)
    fun init() {
        if (kotlin.runCatching {
                Class.forName("org.maxgamer.quickshop.api.shop.AbstractDisplayItem")
            }.isSuccess) {
            GuardItemChecker.registeredIntegrations += this
        }
    }
}

typealias AbstractDisplayItemHikari = com.ghostchu.quickshop.shop.display.AbstractDisplayItem
typealias AbstractDisplayItemReremake = org.maxgamer.quickshop.api.shop.AbstractDisplayItem