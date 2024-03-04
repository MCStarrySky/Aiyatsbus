package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.event

import com.mcstarrysky.aiyatsbus.core.util.calcToDouble
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.*
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageEvent

object EventDamaged : EventEntry<EntityDamageEvent>() {

    override fun modify(event: EntityDamageEvent, entity: LivingEntity, cmd: String, params: List<String>): Boolean {
        when (cmd) {
            "设置伤害" -> {
                val dmg = params[0].calcToDouble()
                if (dmg < 0.0) event.isCancelled = true
                else event.damage = dmg
            }

            "取消伤害" -> event.isCancelled = true
        }
        return true
    }

    override fun get(event: EntityDamageEvent, objName: String): Pair<ObjectEntry<*>, Any?> {
        return when (objName) {
            "受伤者" -> objLivingEntity.holderize(event.entity as LivingEntity)
            "伤害类型" -> objString.h(event.cause.toString())
            "伤害" -> objString.h(event.damage)
            else -> objString.h(null)
        }
    }
}