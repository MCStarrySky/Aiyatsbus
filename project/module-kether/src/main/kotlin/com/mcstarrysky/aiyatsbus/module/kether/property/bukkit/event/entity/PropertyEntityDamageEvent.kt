package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.entity

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.entity.EntityDamageEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.entity.PropertyEntityDamageEvent
 *
 * @author mical
 * @since 2024/3/12 21:38
 */
@AiyatsbusProperty(
    id = "entity-damage-event",
    bind = EntityDamageEvent::class
)
class PropertyEntityDamageEvent : AiyatsbusGenericProperty<EntityDamageEvent>("entity-damage-event") {

    override fun readProperty(instance: EntityDamageEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "damage" -> instance.damage
            "finalDamage", "final-damage" -> instance.finalDamage
            "cause" -> instance.cause.name
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: EntityDamageEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "damage" -> instance.damage = value as Double
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}