package com.mcstarrysky.aiyatsbus.impl.nms17

import com.mcstarrysky.aiyatsbus.core.AiyatsbusMinecraftAPI
import com.mcstarrysky.aiyatsbus.core.toDisplayMode
import com.mcstarrysky.aiyatsbus.core.util.isNull
import com.mcstarrysky.aiyatsbus.impl.nms17.v12005_nms.NMS12005
import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.util.Codec
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.chat.ComponentSerializer
import net.minecraft.network.chat.IChatBaseComponent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_20_R3.util.CraftChatMessage
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.nms.MinecraftVersion
import java.io.IOException

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.nms.DefaultAiyatsbusMinecraftAPI
 *
 * @author mical
 * @since 2024/2/18 00:21
 */
class DefaultAiyatsbusMinecraftAPI17 : AiyatsbusMinecraftAPI {

    init {
        // 预热
        if (MinecraftVersion.majorLegacy >= 12005) {
            NMS12005.instance
        }
    }

    private val gsonComponentSerializer = GsonComponentSerializer.gson()
    private val NBT_CODEC: Codec<Any, String, IOException, IOException> = PaperAdventure::class.java.getProperty("NBT_CODEC", isStatic = true)!!

    override fun getRepairCost(item: ItemStack): Int {
        return if (MinecraftVersion.majorLegacy >= 12005) {
            NMS12005.instance.getRepairCost(item)
        } else {
            (asNMSCopy(item) as NMSItemStack).baseRepairCost
        }
    }

    override fun setRepairCost(item: ItemStack, cost: Int) {
        if (MinecraftVersion.majorLegacy >= 12005) {
            NMS12005.instance.setRepairCost(item, cost)
        } else {
            (asNMSCopy(item) as NMSItemStack).setRepairCost(cost)
        }
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
                    val nmsItem = asNMSCopy(bkItem)
                    val nbt = NBT_CODEC.decode(tag) as NMSNBTTagCompound
                    (nmsItem as NMSItemStack).tag = nbt
                    asBukkitCopy(nmsItem)
                }
            }
        } catch (t: Throwable) {
            throw IllegalStateException(t)
        }
    }

    override fun adaptMerchantRecipe(merchantRecipeList: Any, player: Player): Any {

        fun adapt(item: Any, player: Player): Any {
            val bkItem = asBukkitCopy(item)
            if (bkItem.isNull) return item
            return asNMSCopy(bkItem.toDisplayMode(player))
        }

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
        return adapt
    }

    override fun componentToIChatBaseComponent(component: Component): Any? {
//        return if (MinecraftVersion.majorLegacy >= 12005) {
//            NMS12005.instance.componentToIChatBaseComponent(component)
//        } else IChatBaseComponent.ChatSerializer.fromJson(gsonComponentSerializer.serialize(component))
        return CraftChatMessage.fromJSON(gsonComponentSerializer.serialize(component))
    }

    override fun iChatBaseComponentToComponent(iChatBaseComponent: Any): Component {
//        return if (MinecraftVersion.majorLegacy >= 12005) {
//            NMS12005.instance.iChatBaseComponentToComponent(iChatBaseComponent)
//        } else gsonComponentSerializer.deserialize(IChatBaseComponent.ChatSerializer.toJson(iChatBaseComponent as IChatBaseComponent))
        return gsonComponentSerializer.deserialize(CraftChatMessage.toJSON(iChatBaseComponent as IChatBaseComponent))
    }

    override fun breakBlock(player: Player, block: Block): Boolean {
        return (player as CraftPlayer20).handle.gameMode.destroyBlock(NMSBlockPosition(block.x, block.y, block.z))
    }

    override fun damageItemStack(item: ItemStack, amount: Int, entity: LivingEntity): ItemStack {
        var stack = item
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
        nmsStack as NMSItemStack
        nmsStack.hurtAndBreak(amount, (entity as CraftLivingEntity20).handle) { entityLiving ->
            (enumItemSlot as? NMSEnumItemSlot)?.let { entityLiving.broadcastBreakEvent(it) }
        }
    }

    override fun asNMSCopy(item: ItemStack): Any {
        return CraftItemStack20.asNMSCopy(item)
    }

    override fun asBukkitCopy(item: Any): ItemStack {
        return CraftItemStack20.asBukkitCopy(item as NMSItemStack)
    }

    override fun sendRawActionBar(player: Player, action: String) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *ComponentSerializer.parse(action))
    }
}