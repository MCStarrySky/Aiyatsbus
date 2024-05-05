package com.mcstarrysky.aiyatsbus.impl.nms.v12005_nms

import net.minecraft.core.component.DataComponents
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.NMSItem

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.nms.v12005_nms.NMS12005Impl
 *
 * @author mical
 * @since 2024/5/5 20:26
 */
class NMS12005Impl : NMS12005() {

    override fun getRepairCost(item: ItemStack): Int {
        return (NMSItem.asNMSCopy(item) as net.minecraft.world.item.ItemStack)[DataComponents.REPAIR_COST] ?: 0
    }

    override fun setRepairCost(item: ItemStack, cost: Int) {
        (NMSItem.asNMSCopy(item) as net.minecraft.world.item.ItemStack)[DataComponents.REPAIR_COST] = cost
    }
}