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
package com.mcstarrysky.aiyatsbus.impl.nms.v12005_nms

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common.util.unsafeLazy
import taboolib.module.nms.nmsProxy

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.nms.nms12005.NMS12005
 *
 * @author mical
 * @since 2024/5/5 20:20
 */
abstract class NMS12005 {

    abstract fun getRepairCost(item: ItemStack): Int

    abstract fun setRepairCost(item: ItemStack, cost: Int)

    abstract fun adaptMerchantRecipe(merchantRecipeList: Any, player: Player): Any

    abstract fun hurtAndBreak(nmsItem: Any, amount: Int, entity: LivingEntity)

    /** 隐藏书本附魔 */
    abstract fun hideBookEnchants(item: ItemMeta)

    /** 是否已经隐藏书本附魔 */
    abstract fun isBookEnchantsHidden(item: ItemMeta): Boolean

    /** 显示书本附魔 */
    abstract fun removeBookEnchantsHidden(item: ItemMeta)

    companion object {

        val instance by unsafeLazy { nmsProxy<NMS12005>() }
    }
}