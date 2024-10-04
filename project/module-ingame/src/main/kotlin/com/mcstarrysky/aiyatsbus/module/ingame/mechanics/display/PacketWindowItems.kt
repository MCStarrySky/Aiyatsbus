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
            try {
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
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}