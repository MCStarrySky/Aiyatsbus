package com.mcstarrysky.aiyatsbus.module.ingame.mechanics.display

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.toDisplayMode
import com.mcstarrysky.aiyatsbus.core.util.isNull
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.PacketSendEvent

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.listener.packet.PacketWIndowItems
 *
 * @author mical
 * @since 2024/2/18 00:43
 */
object PacketWindowItems {

    @SubscribeEvent(priority = EventPriority.MONITOR)
    fun e(e: PacketSendEvent) {
        if (e.packet.name == "PacketPlayOutWindowItems" || e.packet.name == "ClientboundContainerSetContentPacket") {
            runCatching {
                val field = if (MinecraftVersion.isUniversal) "items" else "b"
                val slots = e.packet.read<List<Any>>(field)!!.toMutableList()
                for (i in slots.indices) {
                    val bkItem = Aiyatsbus.api().getMinecraftAPI().asBukkitCopy(slots[i])
                    if (bkItem.isNull) continue
                    val nmsItem = Aiyatsbus.api().getMinecraftAPI().asNMSCopy(bkItem.toDisplayMode(e.player))
                    slots[i] = nmsItem
                }
                e.packet.write(field, slots)

                if (MinecraftVersion.major >= 9) {
                    val cursor = e.packet.read<Any>("carriedItem")!! // carriedItem
                    val bkItem = Aiyatsbus.api().getMinecraftAPI().asBukkitCopy(cursor)
                    if (bkItem.isNull) return
                    val nmsItem = Aiyatsbus.api().getMinecraftAPI().asNMSCopy(bkItem.toDisplayMode(e.player))
                    e.packet.write("carriedItem", nmsItem)
                }
            }.onFailure { it.printStackTrace() }
        }
    }
}