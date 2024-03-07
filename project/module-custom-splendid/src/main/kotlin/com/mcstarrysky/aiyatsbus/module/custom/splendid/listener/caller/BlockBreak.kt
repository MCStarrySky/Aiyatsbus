package com.mcstarrysky.aiyatsbus.module.custom.splendid.listener.caller

import com.mcstarrysky.aiyatsbus.core.util.serialized
import com.mcstarrysky.aiyatsbus.module.custom.splendid.TriggerSlots
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.EventType
import com.mcstarrysky.aiyatsbus.module.custom.splendid.triggerEts
import org.bukkit.block.Block
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

    /**
     * 一定要存这个, 在破坏额外方块的时候删除, 避免递归永远挖下去
     */
    val extraBlocks = mutableSetOf<String>()

    fun breakExtra(block: Block) {
        extraBlocks += block.location.serialized
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun lowest(event: BlockBreakEvent) = settle(event, EventPriority.LOWEST)

    @SubscribeEvent(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun high(event: BlockBreakEvent) = settle(event, EventPriority.HIGH)

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun highest(event: BlockBreakEvent) = settle(event, EventPriority.HIGHEST)

    private fun settle(event: BlockBreakEvent, priority: EventPriority) {
        // 检测是否重复, 防止递归
        if (extraBlocks.contains(event.block.location.serialized)) return
        EventType.BREAK.triggerEts(event, priority, TriggerSlots.HANDS, event.player)
    }
}