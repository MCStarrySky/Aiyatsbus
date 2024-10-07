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
package com.mcstarrysky.aiyatsbus.core

import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import kotlin.jvm.Throws

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusMinecraftAPI
 *
 * @author mical
 * @since 2024/2/18 00:14
 */
interface AiyatsbusMinecraftAPI {

    /**
     * 获取物品在铁砧上的操作数
     */
    fun getRepairCost(item: ItemStack): Int

    /**
     * 设置物品在铁砧上的操作数
     */
    fun setRepairCost(item: ItemStack, cost: Int)

    /**
     * 1.18.2 以下版本 (不包含 1.18.2) 中 ItemFactory#createItemStack 不存在
     * 此函数用以替代
     */
    @Throws(IllegalStateException::class)
    fun createItemStack(material: String, tag: String?): ItemStack

    /** 为原版的 MerchantRecipeList 的物品显示更多附魔 */
    fun adaptMerchantRecipe(merchantRecipeList: Any, player: Player): Any

    /** 将 Json 转成 IChatBaseComponent */
    fun componentFromJson(json: String): Any

    /** 将 IChatBaseComponent 转成 Json */
    fun componentToJson(iChatBaseComponent: Any): String

    /** 取代高版本 player.breakBlock 的函数, 会触发 BlockBreakEvent */
    fun breakBlock(player: Player, block: Block): Boolean

    /** 取代高版本 ItemStack#damage */
    fun damageItemStack(item: ItemStack, amount: Int, entity: LivingEntity): ItemStack

    /** 隐藏书本附魔 */
    fun hideBookEnchants(item: ItemMeta)

    /** 是否已经隐藏书本附魔 */
    fun isBookEnchantsHidden(item: ItemMeta): Boolean

    /** 显示书本附魔 */
    fun removeBookEnchantsHidden(item: ItemMeta)

    /** 发送手部活动数据 */
    fun setHandActive(player: Player, isHandActive: Boolean)

    /** 兼容 1.21 */
    fun asNMSCopy(item: ItemStack): Any

    /** 兼容 1.21 */
    fun asBukkitCopy(item: Any): ItemStack

    /** 兼容 1.21 */
    fun sendRawActionBar(player: Player, action: String)
}