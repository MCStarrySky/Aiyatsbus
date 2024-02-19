package com.mcstarrysky.aiyatsbus.module.custom.splendid.listener.caller

import com.mcstarrysky.aiyatsbus.module.custom.splendid.TriggerSlots
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.EventType
import com.mcstarrysky.aiyatsbus.module.custom.splendid.triggerEts
import org.bukkit.event.player.PlayerItemDamageEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

object PlayerItemDamage {

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun highest(event: PlayerItemDamageEvent) = settle(event)

    private fun settle(event: PlayerItemDamageEvent) {
        EventType.DURABILITY_REDUCED.triggerEts(event, EventPriority.HIGHEST, TriggerSlots.ALL, event.player)
    }
}