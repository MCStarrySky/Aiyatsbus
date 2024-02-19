package com.mcstarrysky.aiyatsbus.core

import net.md_5.bungee.api.chat.hover.content.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusMinecraftAPI
 *
 * @author mical
 * @since 2024/2/18 00:14
 */
interface AiyatsbusMinecraftAPI {

    /** 为原版的 MerchantRecipeList 的物品显示更多附魔 */
    fun adaptMerchantRecipe(merchantRecipeList: Any, player: Player): Any

    /** 获取 BungeeCord 物品 Json */
    fun itemToJson(item: Item): String

    /** 获取 Bukkit 物品 Json */
    fun bkItemToJson(item: ItemStack): String

    /** 从 Json 获取 Bukkit 物品 */
    fun jsonToItem(json: String): ItemStack
}