package com.mcstarrysky.aiyatsbus.impl.nms

import com.mcstarrysky.aiyatsbus.core.AiyatsbusMinecraftAPI
import com.mcstarrysky.aiyatsbus.core.toDisplayMode
import com.mcstarrysky.aiyatsbus.core.util.isNull
import com.mcstarrysky.aiyatsbus.impl.nms.v12005_nms.NMS12005
import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.util.Codec
import net.minecraft.network.chat.IChatBaseComponent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.NMSItem
import java.io.IOException

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.nms.DefaultAiyatsbusMinecraftAPI
 *
 * @author mical
 * @since 2024/2/18 00:21
 */
class DefaultAiyatsbusMinecraftAPI : AiyatsbusMinecraftAPI {

    init {
        // 预热
        NMS12005.instance
    }

    private val gsonComponentSerializer = GsonComponentSerializer.gson()
    private val NBT_CODEC: Codec<Any, String, IOException, IOException> = PaperAdventure::class.java.getProperty("NBT_CODEC", isStatic = true)!!

    override fun getRepairCost(item: ItemStack): Int {
        return if (MinecraftVersion.isUniversal) {
            if (MinecraftVersion.majorLegacy >= 12005) {
                NMS12005.instance.getRepairCost(item)
            } else {
                (NMSItem.asNMSCopy(item) as NMSItemStack).baseRepairCost
            }
        } else (NMSItem.asNMSCopy(item) as NMS16ItemStack).repairCost
    }

    override fun setRepairCost(item: ItemStack, cost: Int) {
        if (MinecraftVersion.isUniversal) {
            if (MinecraftVersion.majorLegacy >= 12005) {
                NMS12005.instance.setRepairCost(item, cost)
            } else {
                (NMSItem.asNMSCopy(item) as NMSItemStack).setRepairCost(cost)
            }
        } else (NMSItem.asNMSCopy(item) as NMS16ItemStack).repairCost = cost
    }

    override fun createItemStack(material: String, tag: String?): ItemStack {
        return try {
            if (MinecraftVersion.majorLegacy >= 11802) {
                Bukkit.getItemFactory().createItemStack(material + tag)
            } else {
                val mat = material.split(":")[1].uppercase()
                val bkItem = ItemStack(Material.valueOf(mat), 1)
                if (tag.isNullOrEmpty()) {
                    bkItem
                } else {
                    val nmsItem = NMSItem.asNMSCopy(bkItem)
                    if (MinecraftVersion.isUniversal) {
                        val nbt = NBT_CODEC.decode(tag) as NMSNBTTagCompound
                        (nmsItem as NMSItemStack).tag = nbt
                    } else {
                        val nbt = NBT_CODEC.decode(tag) as NMS16NBTTagCompound
                        (nmsItem as NMS16ItemStack).tag = nbt
                    }
                    NMSItem.asBukkitCopy(nmsItem)
                }
            }
        } catch (t: Throwable) {
            throw IllegalStateException(t)
        }
    }

    override fun adaptMerchantRecipe(merchantRecipeList: Any, player: Player): Any {

        fun adapt(item: Any, player: Player): Any {
            val bkItem = NMSItem.asBukkitCopy(item)
            if (bkItem.isNull) return item
            return bkItem.toDisplayMode(player)
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

    override fun breakBlock(player: Player, block: Block): Boolean {
        return if (MinecraftVersion.isUniversal) {
            (player as CraftPlayer20).handle.gameMode.destroyBlock(NMSBlockPosition(block.x, block.y, block.z))
        } else {
            (player as CraftPlayer16).handle.playerInteractManager.breakBlock(NMS16BlockPosition(block.x, block.y, block.z))
        }
    }

    override fun damageItemStack(item: ItemStack, amount: Int, entity: LivingEntity): ItemStack {
        var stack = item
        if (MinecraftVersion.isUniversal) {
            val nmsStack = if (stack is CraftItemStack20) {
                val handle = stack.getProperty<NMSItemStack>("handle")
                if (handle == null || handle.isEmpty) {
                    return stack
                }
                handle
            } else {
                CraftItemStack20.asNMSCopy(stack).also {
                    stack = CraftItemStack20.asCraftMirror(it)
                }
            }
            damageItemStack(nmsStack, amount, null, entity)
        } else {
            val nmsStack = if (stack is CraftItemStack16) {
                val handle = stack.getProperty<NMS16ItemStack>("handle")
                if (handle == null || handle.isEmpty) {
                    return stack
                }
                handle
            } else {
                CraftItemStack16.asNMSCopy(stack).also {
                    stack = CraftItemStack16.asCraftMirror(it)
                }
            }
            damageItemStack(nmsStack, amount, null, entity)
        }
        return stack
    }

    /*
    /**
     * CraftLivingEntity&damageItemStack(EquipmentSlot, int)
     */
    fun damageItemStack(slot: EquipmentSlot, amount: Int, entity: LivingEntity) {
        if (MinecraftVersion.isUniversal) {
            val nmsSlot = CraftEquipmentSlot20.getNMS(slot)
            damageItemStack((entity as CraftLivingEntity20).handle.getItemBySlot(nmsSlot), amount, nmsSlot, entity)
        } else {
            val nmsSlot = CraftEquipmentSlot16.getNMS(slot)
            damageItemStack((entity as CraftLivingEntity16).handle.getEquipment(nmsSlot), amount, nmsSlot, entity)
        }
    }

     */

    /**
     * CraftLivingEntity#damageItemStack0
     */
    private fun damageItemStack(nmsStack: Any, amount: Int, enumItemSlot: Any?, entity: LivingEntity) {
        if (MinecraftVersion.isUniversal) {
            nmsStack as NMSItemStack
            nmsStack.hurtAndBreak(amount, (entity as CraftLivingEntity20).handle) { entityLiving ->
                (enumItemSlot as? NMSEnumItemSlot)?.let { entityLiving.broadcastBreakEvent(it) }
            }
        } else {
            nmsStack as NMS16ItemStack
            nmsStack.damage(amount, (entity as CraftLivingEntity16).handle) { entityLiving ->
                (enumItemSlot as? NMS16EnumItemSlot)?.let { entityLiving.broadcastItemBreak(it) }
            }
        }
    }
}