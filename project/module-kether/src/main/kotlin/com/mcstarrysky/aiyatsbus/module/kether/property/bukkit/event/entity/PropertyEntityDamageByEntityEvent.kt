package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.entity

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.entity.EntityDamageByEntityEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.entity.PropertyEntityDamageByEntityEvent
 *
 * @author mical
 * @since 2024/3/23 17:46
 */
@AiyatsbusProperty(
    id = "entity-damage-by-entity-event",
    bind = EntityDamageByEntityEvent::class
)
class PropertyEntityDamageByEntityEvent : AiyatsbusGenericProperty<EntityDamageByEntityEvent>("entity-damage-by-entity-event") {

    override fun readProperty(instance: EntityDamageByEntityEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "damager", "attacker" -> instance.damager
            "isCritical", "is-critical", "critical" -> instance.isCritical
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: EntityDamageByEntityEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}