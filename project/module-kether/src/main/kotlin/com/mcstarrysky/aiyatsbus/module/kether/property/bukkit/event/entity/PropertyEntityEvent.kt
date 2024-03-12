package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.entity

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.entity.EntityEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.entity.PropertyEntityEvent
 *
 * @author mical
 * @since 2024/3/12 21:29
 */
@AiyatsbusProperty(
    id = "entity-event",
    bind = EntityEvent::class
)
class PropertyEntityEvent : AiyatsbusGenericProperty<EntityEvent>("entity-event") {

    override fun readProperty(instance: EntityEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "entity" -> instance.entity
            "entityType", "entity-type" -> instance.entityType
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: EntityEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}