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
package com.mcstarrysky.aiyatsbus.module.compat.chat

import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.nms.MinecraftVersion
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
        val player = e.player
        // 1.19 +
        if (e.packet.name == "ClientboundSystemChatPacket") {
            // 1.20.2 + 取消了 adventure$content, 所以要直接修改 IChatBaseComponent
            if (MinecraftVersion.majorLegacy > 12002) {
                val content = e.packet.source.getProperty<Any>("content") ?: return
                e.packet.source.setProperty("content", DisplayReplacer.inst.apply(content, player))
            } else {
                // 1.19 - 1.20.2 是有一个 adventure$content 存储 Adventure Component, 直接修改这个字段即可
                val adventure = e.packet.source.getProperty<Any>("adventure\$content", remap = false) ?: return
                e.packet.source.setProperty("adventure\$content", DisplayReplacer.inst.apply(adventure, player), remap = false)
            }
        }
        // 1.16 - 1.18 的数据包与高版本不同, 要修改 PacketPlayOutChat 的 message, message 是 IChatBaseComponent
        if (e.packet.name == "PacketPlayOutChat") {
            val field = if (MinecraftVersion.isUniversal) "message" else "a"
            val message = e.packet.source.getProperty<Any>(field) ?: return
            e.packet.source.setProperty(field, DisplayReplacer.inst.apply(message, player))
        }
    }
}