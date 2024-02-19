package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.event

import org.bukkit.entity.LivingEntity
import org.bukkit.event.player.PlayerToggleFlightEvent
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.EventEntry
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.ObjectEntry
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objPlayer
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objString

object EventFly : EventEntry<PlayerToggleFlightEvent>() {

    override fun modify(event: PlayerToggleFlightEvent, entity: LivingEntity, cmd: String, params: List<String>): Boolean {
        return true
    }

    override fun get(event: PlayerToggleFlightEvent, objName: String): Pair<ObjectEntry<*>, Any?> {
        return when (objName) {
            "飞行者" -> objPlayer.holderize(event.player)
            else -> objString.h(null)
        }
    }
}