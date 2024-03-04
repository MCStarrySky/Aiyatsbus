package com.mcstarrysky.aiyatsbus.module.custom.splendid.listener.caller

import com.mcstarrysky.aiyatsbus.module.custom.splendid.TriggerSlots
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.EventType
import com.mcstarrysky.aiyatsbus.module.custom.splendid.triggerEts
import org.bukkit.event.block.BlockBreakEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.custom.splendid.listener.caller.BlockBreak
 *
 * @author mical
 * @since 2024/3/4 21:10
 */
object BlockBreak {

    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun lowest(event: BlockBreakEvent) = settle(event, EventPriority.LOWEST)

    @SubscribeEvent(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun high(event: BlockBreakEvent) = settle(event, EventPriority.HIGH)

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun highest(event: BlockBreakEvent) = settle(event, EventPriority.HIGHEST)

    private fun settle(event: BlockBreakEvent, priority: EventPriority) {
        EventType.BREAK.triggerEts(event, priority, TriggerSlots.HANDS, event.player)
    }
}