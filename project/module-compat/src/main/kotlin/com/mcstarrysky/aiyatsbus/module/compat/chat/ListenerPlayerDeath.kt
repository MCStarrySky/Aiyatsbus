package com.mcstarrysky.aiyatsbus.module.compat.chat

import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.compat.vanilla.ListenerPlayerDeath
 *
 * @author mical
 * @since 2024/3/17 12:25
 */
object ListenerPlayerDeath {

    @SubscribeEvent
    fun e(e: PlayerDeathEvent) {
        // 支持死亡界面显示那个物品
        e.deathMessage(DisplayReplacer.inst.apply(e.deathMessage() ?: return, e.entity))
    }
}