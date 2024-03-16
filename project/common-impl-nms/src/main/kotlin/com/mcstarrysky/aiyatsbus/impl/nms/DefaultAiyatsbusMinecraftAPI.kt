package com.mcstarrysky.aiyatsbus.impl.nms

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusMinecraftAPI
import com.mcstarrysky.aiyatsbus.core.util.isNull
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.network.chat.IChatBaseComponent
import org.bukkit.entity.Player
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.NMSItem

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.nms.DefaultAiyatsbusMinecraftAPI
 *
 * @author mical
 * @since 2024/2/18 00:21
 */
class DefaultAiyatsbusMinecraftAPI : AiyatsbusMinecraftAPI {

    private val gsonComponentSerializer = GsonComponentSerializer.gson()

    override fun adaptMerchantRecipe(merchantRecipeList: Any, player: Player): Any {

        fun adapt(item: Any, player: Player): Any {
            val bkItem = NMSItem.asBukkitCopy(item)
            if (bkItem.isNull) return item
            return NMSItem.asNMSCopy(Aiyatsbus.api().getDisplayManager().display(bkItem, player))
        }

        return when (MinecraftVersion.major) {
            // 1.16
            8 -> {
                val previous = merchantRecipeList as NMS16MerchantRecipeList
                val adapt = NMS16MerchantRecipeList()
                for (i in 0 until previous.size) {
                    val recipe = previous[i]!!
                    adapt += NMS16MerchantRecipe(
                        adapt(recipe.buyingItem1, player) as NMS16ItemStack,
                        adapt(recipe.buyingItem2, player) as NMS16ItemStack,
                        adapt(recipe.sellingItem, player) as NMS16ItemStack,
                        recipe.uses,
                        recipe.maxUses,
                        recipe.xp,
                        recipe.priceMultiplier,
                        recipe.demand
                    )
                }
                adapt
            }
            // 1.17, 1.18, 1.19, 1.20
            in 9..12 -> {
                val previous = merchantRecipeList as NMSMerchantRecipeList
                val adapt = NMSMerchantRecipeList()
                for (i in 0 until previous.size) {
                    val recipe = previous[i]!!
                    adapt += NMSMerchantRecipe(
                        adapt(recipe.baseCostA, player) as NMSItemStack,
                        adapt(recipe.costB, player) as NMSItemStack,
                        adapt(recipe.result, player) as NMSItemStack,
                        recipe.uses,
                        recipe.maxUses,
                        recipe.xp,
                        recipe.priceMultiplier,
                        recipe.demand
                    )
                }
                adapt
            }
            // Unsupported
            else -> error("Unsupported version.")
        }
    }

    override fun componentToIChatBaseComponent(component: Component): Any? {
        return if (MinecraftVersion.isUniversal) {
            IChatBaseComponent.ChatSerializer.fromJson(gsonComponentSerializer.serialize(component))
        } else {
            net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer.b(gsonComponentSerializer.serialize(component))
        }
    }

    override fun iChatBaseComponentToComponent(iChatBaseComponent: Any): Component {
        return if (MinecraftVersion.isUniversal) {
            gsonComponentSerializer.deserialize(IChatBaseComponent.ChatSerializer.toJson(iChatBaseComponent as IChatBaseComponent))
        } else {
            gsonComponentSerializer.deserialize(net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer.a(iChatBaseComponent as net.minecraft.server.v1_16_R3.IChatBaseComponent))
        }
    }
}