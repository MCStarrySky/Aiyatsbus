package com.mcstarrysky.aiyatsbus.module.listener.packet

import com.mcstarrysky.aiyatsbus.core.util.AdventureUtils
import com.mcstarrysky.aiyatsbus.core.util.ComponentUtils
import org.bukkit.entity.Player
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.chat.ComponentText
import taboolib.module.chat.Components
import taboolib.module.nms.PacketSendEvent

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.listener.packet.PacketSystemChat
 *
 * @author mical
 * @since 2024/2/18 00:40
 */
object PacketSystemChat {

    /*
    private val isPaper: Boolean by unsafeLazy {
        runCatching { Class.forName("com.destroystokyo.paper.PaperConfig") }.isSuccess ||
                runCatching { Class.forName("io.papermc.paper.configuration.Configuration") }.isSuccess
    } */

    // @SubscribeEvent(priority = EventPriority.MONITOR) FIXME: 待修复
    fun e(e: PacketSendEvent) {
        if (e.packet.name == "ClientboundSystemChatPacket") {
            runCatching {
                val player = e.player
                // if (isPaper) {
                val adventure = e.packet.read<Any>("adventure\$content") ?: return
                val taboo = AdventureUtils.toTabooLibComponent(adventure) as ComponentText
                val result = AdventureUtils.fromTabooLibComponent(modify(taboo, player))
                e.packet.write("adventure\$content", result)
                // } else {
                //     val taboo = Components.parseRaw(e.packet.read<String>("content") ?: return)
                //     e.packet.write("content", modify(taboo, player).toRawMessage())
                // }
            }.onFailure { it.printStackTrace() }
        }
    }

    private fun modify(taboo: ComponentText, player: Player): ComponentText {
        return Components.parseRaw(ComponentUtils.parseRaw(ComponentUtils.applyItemDisplay(taboo.toSpigotObject(), player)))
    }
}