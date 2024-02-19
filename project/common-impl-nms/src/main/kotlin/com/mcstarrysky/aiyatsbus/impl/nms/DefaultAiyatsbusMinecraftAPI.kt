package com.mcstarrysky.aiyatsbus.impl.nms

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusMinecraftAPI
import com.mcstarrysky.aiyatsbus.core.util.isNull
import com.mojang.brigadier.StringReader
import net.md_5.bungee.api.chat.hover.content.Item
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.severe
import taboolib.library.reflex.Reflex.Companion.invokeConstructor
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.NMSItem
import taboolib.module.nms.nmsClass

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.nms.DefaultAiyatsbusMinecraftAPI
 *
 * @author mical
 * @since 2024/2/18 00:21
 */
class DefaultAiyatsbusMinecraftAPI : AiyatsbusMinecraftAPI {

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

    override fun itemToJson(item: Item): String {
        return runCatching {
            nmsClass("MojangsonParser").invokeConstructor(StringReader(item.tag.nbt))
                .invokeMethod<Any>(if (MinecraftVersion.majorLegacy >= 11800) "readSingleStruct" else "a")?.toString() ?: "{}"
        }.getOrElse {
            severe("Failed to parse item to json: $it")
            "{}"
        }
    }

    override fun bkItemToJson(item: ItemStack): String {
        return runCatching {
            NMSItem.asNMSCopy(item).invokeMethod<Any>("save", nmsClass("NBTTagCompound").newInstance()).toString()
        }.getOrElse {
            severe("Failed to parse bukkit item to json: $it")
            "{}"
        }
    }

    override fun jsonToItem(json: String): ItemStack {
        return runCatching {
            val nbt = nmsClass("MojangsonParser").invokeMethod<Any>(if (MinecraftVersion.majorLegacy >= 11800) "parseTag" else "parse", json, isStatic = true)
            NMSItem.asBukkitCopy(nmsClass("ItemStack").invokeConstructor(nbt))
        }.getOrElse {
            severe("Failed to parse json to item: $it")
            ItemStack(Material.AIR)
        }
    }
}