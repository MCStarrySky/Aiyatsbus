package com.mcstarrysky.aiyatsbus.module.custom.splendid.listener.caller

import com.mcstarrysky.aiyatsbus.module.custom.splendid.TriggerSlots
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.EventType
import com.mcstarrysky.aiyatsbus.module.custom.splendid.triggerEts
import org.bukkit.event.player.PlayerToggleFlightEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

object PlayerToggleFlight {

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun highest(event: PlayerToggleFlightEvent) = settle(event)

    private fun settle(event: PlayerToggleFlightEvent) {
        if (!event.player.isFlying)
            EventType.FLY.triggerEts(event, EventPriority.HIGHEST, TriggerSlots.ALL, event.player)
    }
}