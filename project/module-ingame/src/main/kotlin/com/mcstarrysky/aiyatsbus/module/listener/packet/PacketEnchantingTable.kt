package com.mcstarrysky.aiyatsbus.module.listener.packet

import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.PacketSendEvent

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.listener.packet.PacketEnchantingTable
 *
 * @author mical
 * @since 2024/2/18 00:34
 */
object PacketEnchantingTable {

    @SubscribeEvent(priority = EventPriority.MONITOR)
    fun e(e: PacketSendEvent) {
        if (e.packet.name == "PacketPlayOutWindowData") {
            runCatching {
                val a = e.packet.read<Int>("b", false)
                if (a in 4..6) {
                    e.packet.write("c", -1)
                }
            }.onFailure { it.printStackTrace() }
        }
    }
}