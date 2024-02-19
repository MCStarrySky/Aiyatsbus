package com.mcstarrysky.aiyatsbus.module.custom.splendid.listener.caller

import com.mcstarrysky.aiyatsbus.module.custom.splendid.TriggerSlots
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.EventType
import com.mcstarrysky.aiyatsbus.module.custom.splendid.triggerEts
import org.bukkit.event.player.PlayerToggleSneakEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

object PlayerToggleSneak {

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun highest(event: PlayerToggleSneakEvent) = settle(event)

    private fun settle(event: PlayerToggleSneakEvent) {
        if (!event.player.isSneaking)
            EventType.SNEAK.triggerEts(event, EventPriority.HIGHEST, TriggerSlots.ALL, event.player)
    }
}