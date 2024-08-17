package com.mcstarrysky.aiyatsbus.impl.nms17.v12005_nms

import net.kyori.adventure.text.Component
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

    /** 又改 */
    abstract fun componentToIChatBaseComponent(component: Component): Any?

    /** 又改 */
    abstract fun iChatBaseComponentToComponent(iChatBaseComponent: Any): Component

    companion object {

        val instance by unsafeLazy { nmsProxy<NMS12005>() }
    }
}