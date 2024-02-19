package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.event

import org.bukkit.entity.LivingEntity
import org.bukkit.event.player.PlayerItemDamageEvent
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.*

object EventDurabilityReduced : EventEntry<PlayerItemDamageEvent>() {
    override fun modify(
        event: PlayerItemDamageEvent,
        entity: LivingEntity,
        cmd: String,
        params: List<String>
    ): Boolean {
        when (cmd) {
            //"设置损耗值" -> 见 ObjectItem.modify."损耗耐久"
            "取消损耗" -> event.isCancelled = true
        }
        return true
    }

    override fun get(event: PlayerItemDamageEvent, objName: String): Pair<ObjectEntry<*>, Any?> {
        return when (objName) {
            "所有者" -> objLivingEntity.holderize(event.player)
            "物品" -> event.item.let { objItem.holderize(it) }
            "损耗值" -> objString.h(event.damage)
            "原始损耗值" -> objString.h(event.originalDamage) // 见 https://jd.papermc.io/paper/1.20/org/bukkit/event/player/PlayerItemDamageEvent.html
            else -> objString.h(null)
        }
    }
}