package com.mcstarrysky.aiyatsbus.impl.nms17.v12005_nms

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.core.IRegistryCustom
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.IChatBaseComponent
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.nms.v12005_nms.NMS12005Impl
 *
 * @author mical
 * @since 2024/5/5 20:26
 */
class NMS12005Impl : NMS12005() {

    private val gsonComponentSerializer = GsonComponentSerializer.gson()

    override fun getRepairCost(item: ItemStack): Int {
        return (CraftItemStack.asNMSCopy(item) as net.minecraft.world.item.ItemStack)[DataComponents.REPAIR_COST] ?: 0
    }

    override fun setRepairCost(item: ItemStack, cost: Int) {
        (CraftItemStack.asNMSCopy(item) as net.minecraft.world.item.ItemStack)[DataComponents.REPAIR_COST] = cost
    }

    override fun componentToIChatBaseComponent(component: Component): Any? {
        return IChatBaseComponent.ChatSerializer.fromJson(gsonComponentSerializer.serialize(component), IRegistryCustom.EMPTY)
    }

    override fun iChatBaseComponentToComponent(iChatBaseComponent: Any): Component {
        return gsonComponentSerializer.deserialize(IChatBaseComponent.ChatSerializer.toJson(iChatBaseComponent as IChatBaseComponent, IRegistryCustom.EMPTY))
    }
}