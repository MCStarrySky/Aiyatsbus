package com.mcstarrysky.aiyatsbus.impl.nms.v12005_nms

import org.bukkit.inventory.ItemStack
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

    companion object {

        val instance by unsafeLazy { nmsProxy<NMS12005>() }
    }
}