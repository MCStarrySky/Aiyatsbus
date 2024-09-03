package com.mcstarrysky.aiyatsbus.module.ingame.mechanics.display

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.toRevertMode
import com.mcstarrysky.aiyatsbus.core.util.isNull
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.PacketReceiveEvent

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.listener.packet.PacketSetCreativeSlot
 *
 * @author mical
 * @since 2024/2/18 00:35
 */
object PacketSetCreativeSlot {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun e(e: PacketReceiveEvent) {
        if (e.packet.name == "PacketPlayInSetCreativeSlot" || e.packet.name == "ServerboundSetCreativeModeSlotPacket") {
            try {
                val origin = e.packet.read<Any>("itemStack", true)!!
                val bkItem = Aiyatsbus.api().getMinecraftAPI().asBukkitCopy(origin)
                if (bkItem.isNull) return
                val adapted = Aiyatsbus.api().getMinecraftAPI().asNMSCopy(bkItem.toRevertMode(e.player))
                e.packet.write("itemStack", adapted)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}