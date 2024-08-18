package com.mcstarrysky.aiyatsbus.impl.nms.v12005_nms

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