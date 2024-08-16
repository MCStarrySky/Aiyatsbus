package com.mcstarrysky.aiyatsbus.impl.nms.v12005_nms

import net.minecraft.core.component.DataComponents
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.nms.v12005_nms.NMS12005Impl
 *
 * @author mical
 * @since 2024/5/5 20:26
 */
class NMS12005Impl : NMS12005() {

    override fun getRepairCost(item: ItemStack): Int {
        return (CraftItemStack.asNMSCopy(item) as net.minecraft.world.item.ItemStack)[DataComponents.REPAIR_COST] ?: 0
    }

    override fun setRepairCost(item: ItemStack, cost: Int) {
        (CraftItemStack.asNMSCopy(item) as net.minecraft.world.item.ItemStack)[DataComponents.REPAIR_COST] = cost
    }
}