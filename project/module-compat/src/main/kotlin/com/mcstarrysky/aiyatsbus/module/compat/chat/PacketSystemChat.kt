package com.mcstarrysky.aiyatsbus.module.compat.chat

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import net.kyori.adventure.text.Component
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
                val adventure = Aiyatsbus.api().getMinecraftAPI().iChatBaseComponentToComponent(e.packet.source.getProperty<Any>("content") ?: return)
                try {
                    e.packet.source.setProperty("content", Aiyatsbus.api().getMinecraftAPI().componentToIChatBaseComponent(
                        DisplayReplacer.inst.apply(adventure, player)
                    ))
                } catch (_: Throwable) {
                    // FIXME: 小张看这里有一个 a/adventure 指向 Adventure Component, 但我怎么都测试不出来这个字段
                    e.packet.source.setProperty("a/adventure", DisplayReplacer.inst.apply(adventure, player), remap = false)
                }
            } else {
                // 1.19 - 1.20.2 是有一个 adventure$content 存储 Adventure Component, 直接修改这个字段即可
                val adventure = e.packet.source.getProperty<Any>("adventure\$content", remap = false) as? Component ?: return
                e.packet.source.setProperty("adventure\$content", DisplayReplacer.inst.apply(adventure, player), remap = false)
            }
        }
        // 1.16 - 1.18 的数据包与高版本不同, 要修改 PacketPlayOutChat 的 message, message 是 IChatBaseComponent
        if (e.packet.name == "PacketPlayOutChat") {
            val field = if (MinecraftVersion.isUniversal) "message" else "a"
            val adventure = Aiyatsbus.api().getMinecraftAPI().iChatBaseComponentToComponent(e.packet.source.getProperty<Any>(field) ?: return)
            e.packet.source.setProperty(field, Aiyatsbus.api().getMinecraftAPI().componentToIChatBaseComponent(
                DisplayReplacer.inst.apply(adventure, player)
            ))
        }
    }
}