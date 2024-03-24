package com.mcstarrysky.aiyatsbus.module.ingame.listener.packet

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.util.isNull
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.NMSItem
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
        if (e.packet.name == "PacketPlayOutWindowItems") {
            runCatching {
                val field = when (MinecraftVersion.major) {
                    8 -> "b" // 1.16 -> b
                    in 9..12 -> "c" // 1.17, 1.18, 1.19, 1.20 -> c
                    else -> error("Unsupported version.") // Unsupported
                }
                val slots = e.packet.read<List<Any>>(field, false)!!.toMutableList()
                for (i in slots.indices) {
                    val bkItem = NMSItem.asBukkitCopy(slots[i])
                    if (bkItem.isNull) continue
                    val nmsItem = NMSItem.asNMSCopy(Aiyatsbus.api().getDisplayManager().display(bkItem, e.player))
                    slots[i] = nmsItem
                }
                e.packet.write(field, slots)

                if (MinecraftVersion.major in 9..12) {
                    val cursor = e.packet.read<Any>("d", false)!!
                    val bkItem = NMSItem.asBukkitCopy(cursor)
                    if (bkItem.isNull) return
                    val nmsItem = NMSItem.asNMSCopy(Aiyatsbus.api().getDisplayManager().display(bkItem, e.player))
                    e.packet.write("d", nmsItem)
                }
            }.onFailure { it.printStackTrace() }
        }
    }
}