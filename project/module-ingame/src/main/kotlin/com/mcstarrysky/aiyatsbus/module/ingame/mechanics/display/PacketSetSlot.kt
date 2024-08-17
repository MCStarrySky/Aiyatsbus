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
 * com.mcstarrysky.aiyatsbus.module.listener.packet.PacketSetSlot
 *
 * @author mical
 * @since 2024/2/18 00:38
 */
object PacketSetSlot {

    @SubscribeEvent(priority = EventPriority.MONITOR)
    fun e(e: PacketSendEvent) {
        if (e.packet.name == "PacketPlayOutSetSlot" || e.packet.name == "ClientboundContainerSetSlotPacket") {
            runCatching {
                val field = if (MinecraftVersion.isUniversal) "itemStack" else "c"
                val origin = e.packet.read<Any>(field)!!
                val bkItem = Aiyatsbus.api().getMinecraftAPI().asBukkitCopy(origin)
                if (bkItem.isNull) return
                val adapted = Aiyatsbus.api().getMinecraftAPI().asNMSCopy(bkItem.toDisplayMode(e.player))
                e.packet.write(field, adapted)
            }.onFailure { it.printStackTrace() }
        }
    }
}