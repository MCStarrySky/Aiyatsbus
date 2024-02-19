package com.mcstarrysky.aiyatsbus.module.custom.splendid.listener.caller

import com.mcstarrysky.aiyatsbus.module.custom.splendid.TriggerSlots
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.EventType
import com.mcstarrysky.aiyatsbus.module.custom.splendid.triggerEts
import org.bukkit.event.player.PlayerChatEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

object PlayerChat {
    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun highest(event: PlayerChatEvent) = EventType.CHAT.triggerEts(event, EventPriority.HIGHEST, TriggerSlots.ALL, event.player)
}
