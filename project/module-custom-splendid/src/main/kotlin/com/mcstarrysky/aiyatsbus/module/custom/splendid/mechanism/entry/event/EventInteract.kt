package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.event

import org.bukkit.entity.LivingEntity
import org.bukkit.event.player.PlayerInteractEvent
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.*

//ONLY PLAYER
object EventInteract : EventEntry<PlayerInteractEvent>() {

    override fun modify(event: PlayerInteractEvent, entity: LivingEntity, cmd: String, params: List<String>): Boolean {
        when (cmd) {
            "取消交互" -> event.isCancelled = true
            else -> {}
        }
        return true
    }

    override fun get(event: PlayerInteractEvent, objName: String): Pair<ObjectEntry<*>, Any?> {
        return when (objName) {
            "主副手" -> objString.h(event.hand)
            "交互方块" -> event.clickedBlock?.let { objBlock.holderize(it) } ?: (objBlock to null)
            "手持物品" -> event.item?.let { objItem.holderize(it) } ?: (objItem to null)
            "玩家" -> objPlayer.holderize(event.player)
            else -> objString.h(null)
        }
    }
}