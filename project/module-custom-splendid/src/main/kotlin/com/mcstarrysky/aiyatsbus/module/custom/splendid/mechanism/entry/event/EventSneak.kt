package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.event

import org.bukkit.entity.LivingEntity
import org.bukkit.event.player.PlayerToggleSneakEvent
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.EventEntry
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.ObjectEntry
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objPlayer
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objString

object EventSneak : EventEntry<PlayerToggleSneakEvent>() {

    override fun modify(event: PlayerToggleSneakEvent, entity: LivingEntity, cmd: String, params: List<String>): Boolean {
        return true
    }

    override fun get(event: PlayerToggleSneakEvent, objName: String): Pair<ObjectEntry<*>, Any?> {
        return when (objName) {
            "下蹲者" -> objPlayer.holderize(event.player)
            else -> objString.h(null)
        }
    }
}