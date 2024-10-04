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

import com.mcstarrysky.aiyatsbus.core.toDisplayMode
import com.mcstarrysky.aiyatsbus.core.util.isNull
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.trading.MerchantRecipe
import net.minecraft.world.item.trading.MerchantRecipeList
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftLivingEntity
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import taboolib.library.reflex.Reflex.Companion.setProperty
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.nms.v12005_nms.NMS12005Impl
 *
 * @author mical
 * @since 2024/5/5 20:26
 */
class NMS12005Impl : NMS12005() {

    override fun getRepairCost(item: ItemStack): Int {
        return (CraftItemStack.asNMSCopy(item) as NMSItemStack)[DataComponents.REPAIR_COST] ?: 0
    }

    override fun setRepairCost(item: ItemStack, cost: Int) {
        (CraftItemStack.asNMSCopy(item) as NMSItemStack)[DataComponents.REPAIR_COST] = cost
    }

    override fun adaptMerchantRecipe(merchantRecipeList: Any, player: Player): Any {

        fun adapt(item: Any, player: Player): Any {
            val bkItem = CraftItemStack.asBukkitCopy(item as NMSItemStack)
            if (bkItem.isNull) return item
            return CraftItemStack.asNMSCopy(bkItem.toDisplayMode(player))
        }

        val previous = merchantRecipeList as MerchantRecipeList
        val adapt = MerchantRecipeList()
        for (i in 0 until previous.size) {
            val recipe = previous[i]!!
            val baseCostA = recipe.baseCostA.also { it.setProperty("itemStack", adapt(it.itemStack, player)) }
            val costB = Optional.ofNullable(recipe.costB.getOrNull()?.also { it.setProperty("itemStack", adapt(it.itemStack, player)) })
            adapt += MerchantRecipe(
                baseCostA,
                costB,
                adapt(recipe.result, player) as NMSItemStack,
                recipe.uses,
                recipe.maxUses,
                recipe.xp,
                recipe.priceMultiplier,
                recipe.demand
            )
        }
        return adapt
    }

    override fun hurtAndBreak(nmsItem: Any, amount: Int, entity: LivingEntity) {
        return (nmsItem as NMSItemStack).hurtAndBreak(amount, (entity as CraftLivingEntity).handle, null)
    }

    override fun hideBookEnchants(item: ItemMeta) {
        item.addItemFlags(ItemFlag.HIDE_STORED_ENCHANTS)
        // Spigot: ItemFlag.HIDE_ADDITIONAL_TOOLTIP
    }

    override fun isBookEnchantsHidden(item: ItemMeta): Boolean {
        return item.hasItemFlag(ItemFlag.HIDE_STORED_ENCHANTS)
    }

    override fun removeBookEnchantsHidden(item: ItemMeta) {
        item.removeItemFlags(ItemFlag.HIDE_STORED_ENCHANTS)
    }
}

typealias NMSItemStack = net.minecraft.world.item.ItemStack