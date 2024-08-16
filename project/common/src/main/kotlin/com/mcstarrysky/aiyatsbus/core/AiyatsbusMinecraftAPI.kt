package com.mcstarrysky.aiyatsbus.core

import net.kyori.adventure.text.Component
import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
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

    /** 将 Adventure Component 转成 IChatBaseComponent */
    fun componentToIChatBaseComponent(component: Component): Any?

    /** 将 IChatBaseComponent 转成 Adventure Component */
    fun iChatBaseComponentToComponent(iChatBaseComponent: Any): Component

    /** 取代高版本 player.breakBlock 的函数, 会触发 BlockBreakEvent */
    fun breakBlock(player: Player, block: Block): Boolean

    /** 取代高版本 ItemStack#damage */
    fun damageItemStack(item: ItemStack, amount: Int, entity: LivingEntity): ItemStack

    /** 兼容 1.21 */
    fun asNMSCopy(item: ItemStack): Any

    /** 兼容 1.21 */
    fun asBukkitCopy(item: Any): ItemStack

    /** 兼容 1.21 */
    fun sendRawActionBar(player: Player, action: String)
}