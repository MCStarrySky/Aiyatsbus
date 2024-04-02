package com.mcstarrysky.aiyatsbus.module.ingame.listener.mechanism

import org.bukkit.entity.Player
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.lang.event.PlayerSelectLocaleEvent
import taboolib.module.lang.event.SystemSelectLocaleEvent
import taboolib.platform.util.onlinePlayers

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.ingame.listener.mechanism.LocaleSelectListener
 *
 * @author mical
 * @since 2024/4/2 21:22
 */
object LocaleSelectListener {

    @SubscribeEvent
    fun e(e: PlayerSelectLocaleEvent) {
        e.player.castSafely<Player>()?.updateInventory()
    }

    @SubscribeEvent
    fun e(e: SystemSelectLocaleEvent) {
        onlinePlayers.forEach(Player::updateInventory)
    }
}