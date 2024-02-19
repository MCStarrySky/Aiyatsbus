package com.mcstarrysky.aiyatsbus.core

import com.mcstarrysky.aiyatsbus.core.data.PlayerData
import org.bukkit.entity.Player

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusPlayerDataHandler
 *
 * @author mical
 * @since 2024/2/18 12:57
 */
interface AiyatsbusPlayerDataHandler {

    fun load(player: Player)

    fun save(player: Player)

    fun get(player: Player): PlayerData
}