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
 * com.mcstarrysky.aiyatsbus.module.listener.packet.PacketSetSlot
 *
 * @author mical
 * @since 2024/2/18 00:38
 */
object PacketSetSlot {

    @SubscribeEvent(priority = EventPriority.MONITOR)
    fun e(e: PacketSendEvent) {
        if (e.packet.name == "PacketPlayOutSetSlot") {
            runCatching {
                val field = when (MinecraftVersion.major) {
                    8 -> "c" // 1.16 -> b
                    in 9..12 -> "f" // 1.17, 1.18, 1.19, 1.20 -> c
                    else -> error("Unsupported version.") // Unsupported
                }
                val origin = e.packet.read<Any>(field, false)!!
                val bkItem = NMSItem.asBukkitCopy(origin)
                if (bkItem.isNull) return
                val adapted = NMSItem.asNMSCopy(Aiyatsbus.api().getDisplayManager().display(bkItem, e.player))
                e.packet.write(field, adapted)
            }.onFailure { it.printStackTrace() }
        }
    }
}