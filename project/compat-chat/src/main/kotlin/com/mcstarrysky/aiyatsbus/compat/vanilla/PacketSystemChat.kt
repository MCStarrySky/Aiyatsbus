package com.mcstarrysky.aiyatsbus.compat.vanilla

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.nbt.api.BinaryTagHolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEvent.ShowItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.nms.NMSItem
import taboolib.module.nms.PacketSendEvent

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.compat.vanilla.PacketSystemChat
 *
 * @author mical
 * @since 2024/2/18 00:40
 */
object PacketSystemChat {

    @SubscribeEvent(priority = EventPriority.MONITOR)
    fun e(e: PacketSendEvent) {
        if (e.packet.name == "ClientboundSystemChatPacket") {
            runCatching {
                val player = e.player
                val adventure = e.packet.source.getProperty<Any>("adventure\$content", remap = false) as? Component ?: return
                e.packet.source.setProperty("adventure\$content", modify(adventure, player), remap = false)
            }.onFailure { it.printStackTrace() }
        }
    }

    private fun modify(c: Component, player: Player): Component {
        val component = Component.empty()
        if (c is TranslatableComponent) {
            c.args().forEach { component.append(modifyOne(it, player)) }
            return component
        }
        // TODO: component.children().?

        return component
    }

    private fun modifyOne(component: Component, player: Player): Component {
        val hover = component.hoverEvent()
        if (hover != null && hover.value() is ShowItem) {
            val value = hover.value() as ShowItem
            val nbt = value.nbt()
            if (nbt != null) {
                val originNbt = PaperAdventure::class.java.getProperty<Any>("NBT_CODEC", isStatic = true)!!.invokeMethod<Any>("decode", nbt.string())
                val itemStack = ItemStack(Material.valueOf(value.item().value().uppercase()), 1)
                val nmsItem = NMSItem.asNMSCopy(itemStack)
                nmsItem.invokeMethod<Any>("setTag", originNbt)
                val bkItem = NMSItem.asBukkitCopy(nmsItem)

                val modified = Aiyatsbus.api().getDisplayManager().display(bkItem, player)
                val editedTag = PaperAdventure::class.java.invokeMethod<BinaryTagHolder>("asBinaryTagHolder", NMSItem.asNMSCopy(modified).invokeMethod("getTag"), isStatic = true)
                // value.invokeMethod<Any>("nbt", editedTag) // 懒得判断版本了
                return component.hoverEvent(HoverEvent.showItem(value.item(), value.count(), editedTag))
            }
        }
        return component
    }
}