package com.mcstarrysky.aiyatsbus.module.listener.packet

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.PacketSendEvent

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.listener.packet.PacketOpenWindowMerchant
 *
 * @author mical
 * @since 2024/2/18 00:34
 */
object PacketOpenWindowMerchant {

    @SubscribeEvent(priority = EventPriority.MONITOR)
    fun e(e: PacketSendEvent) {
        if (e.packet.name == "PacketPlayOutOpenWindowMerchant") {
            runCatching {
                val merchant = e.packet.read<Any>("b", false)!!
                e.packet.write("b", Aiyatsbus.api().getMinecraftAPI().adaptMerchantRecipe(merchant, e.player))
            }.onFailure { it.printStackTrace() }
        }
    }
}