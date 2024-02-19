package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.event

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import taboolib.platform.util.attacker
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.*

object EventAttack : EventEntry<EntityDamageByEntityEvent>() {

    override fun modify(event: EntityDamageByEntityEvent, entity: LivingEntity, cmd: String, params: List<String>): Boolean {
        when (cmd) {
            else -> EventDamaged.modify(event, event.entity as LivingEntity, cmd, params)
        }
        return true
    }

    override fun get(event: EntityDamageByEntityEvent, objName: String): Pair<ObjectEntry<*>, Any?> {
        val attacker = event.attacker
        val damaged = event.entity
        return when (objName) {
            "伤害" -> objString.h(event.damage)
            "攻击者" -> objLivingEntity.holderize(attacker!!)
            "受击者" -> objEntity.holderize(damaged)
            "蓄能程度" -> objString.h((attacker as? Player)?.attackCooldown ?: 1.0f)
            "是否暴击" -> objString.h((attacker?.fallDistance ?: -1f) > 0)
            "是否格挡" -> objString.h((damaged as? Player)?.isBlocking ?: false)
            else -> EventDamaged[event, objName]
        }
    }
}