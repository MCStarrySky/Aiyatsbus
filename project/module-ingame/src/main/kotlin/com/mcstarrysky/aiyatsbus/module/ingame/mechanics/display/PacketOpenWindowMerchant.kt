/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.mcstarrysky.aiyatsbus.module.ingame.mechanics.display

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.MinecraftVersion
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
        if (e.packet.name == "PacketPlayOutOpenWindowMerchant" || e.packet.name == "ClientboundMerchantOffersPacket") {
            try {
                // 1.16 - 1.20.4 全部版本都可以直接读 b, 1.20.5 改成 c
                val field = if (MinecraftVersion.isUniversal) "offers" else "b"
                val merchant = e.packet.read<Any>(field, MinecraftVersion.isUniversal)!!
                e.packet.write(field, Aiyatsbus.api().getMinecraftAPI().adaptMerchantRecipe(merchant, e.player))
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}