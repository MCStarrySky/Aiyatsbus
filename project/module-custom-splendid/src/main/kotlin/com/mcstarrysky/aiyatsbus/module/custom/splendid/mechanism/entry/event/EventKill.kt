package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.event

import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import taboolib.platform.util.attacker
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.EventEntry
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.ObjectEntry
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objLivingEntity
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objString

object EventKill : EventEntry<EntityDamageByEntityEvent>() {

    override fun modify(event: EntityDamageByEntityEvent, entity: LivingEntity, cmd: String, params: List<String>): Boolean {
        return true
    }

    override fun get(event: EntityDamageByEntityEvent, objName: String): Pair<ObjectEntry<*>, Any?> {
        return when (objName) {
            "击杀者" -> objLivingEntity.holderize(event.attacker!!)
            "死者" -> objLivingEntity.holderize(event.entity as LivingEntity)
            else -> objString.h(null)
        }
    }
}