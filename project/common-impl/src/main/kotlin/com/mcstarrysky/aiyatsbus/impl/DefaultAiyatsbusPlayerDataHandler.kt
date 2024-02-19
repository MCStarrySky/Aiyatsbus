package com.mcstarrysky.aiyatsbus.impl

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusPlayerDataHandler
import com.mcstarrysky.aiyatsbus.core.data.PlayerData
import com.mcstarrysky.aiyatsbus.core.util.get
import com.mcstarrysky.aiyatsbus.core.util.set
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.persistence.PersistentDataType
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.Schedule
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.platform.util.onlinePlayers
import java.util.*

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.DefaultAiyatsbusPlayerDataHandler
 *
 * @author mical
 * @since 2024/2/18 13:02
 */
class DefaultAiyatsbusPlayerDataHandler : AiyatsbusPlayerDataHandler {

    private val data = mutableMapOf<UUID, PlayerData>()

    override fun load(player: Player) {
        data[player.uniqueId] = PlayerData(player["aiyatsbus_data", PersistentDataType.STRING])
    }

    override fun save(player: Player) {
        data[player.uniqueId]?.let { player["aiyatsbus_data", PersistentDataType.STRING] = it.serialize() }
    }

    override fun get(player: Player): PlayerData {
        return data[player.uniqueId]!!
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusPlayerDataHandler>(DefaultAiyatsbusPlayerDataHandler())

            registerLifeCycleTask(LifeCycle.ENABLE, 5) {
                val api = PlatformFactory.getAPI<AiyatsbusPlayerDataHandler>()
                onlinePlayers.forEach(api::load)
            }
        }

        @Schedule(period = 600L)
        fun tick() {
            onlinePlayers.forEach { Aiyatsbus.api().getPlayerDataHandler().save(it) }
        }

        @SubscribeEvent(priority = EventPriority.MONITOR)
        fun e(e: PlayerJoinEvent) {
            Aiyatsbus.api().getPlayerDataHandler().load(e.player)
        }

        @SubscribeEvent(priority = EventPriority.MONITOR)
        fun e(e: PlayerQuitEvent) {
            Aiyatsbus.api().getPlayerDataHandler().save(e.player)
        }
    }
}