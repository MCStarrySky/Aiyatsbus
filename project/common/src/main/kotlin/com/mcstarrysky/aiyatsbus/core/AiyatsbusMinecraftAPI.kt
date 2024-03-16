package com.mcstarrysky.aiyatsbus.core

import net.kyori.adventure.text.Component
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

    /** 将 Adventure Component 转成 IChatBaseComponent */
    fun componentToIChatBaseComponent(component: Component): Any?

    /** 将 IChatBaseComponent 转成 Adventure Component */
    fun iChatBaseComponentToComponent(iChatBaseComponent: Any): Component
}