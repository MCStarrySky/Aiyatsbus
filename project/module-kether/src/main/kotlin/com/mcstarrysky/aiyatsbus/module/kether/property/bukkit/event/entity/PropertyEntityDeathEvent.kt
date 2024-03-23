package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.entity

import com.mcstarrysky.aiyatsbus.core.util.coerceDouble
import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.entity.EntityDeathEvent
import taboolib.common.OpenResult
import taboolib.platform.util.killer

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.entity.PropertyEntityDeathEvent
 *
 * @author mical
 * @since 2024/3/23 21:06
 */
@AiyatsbusProperty(
    id = "entity-death-event",
    bind = EntityDeathEvent::class
)
class PropertyEntityDeathEvent : AiyatsbusGenericProperty<EntityDeathEvent>("entity-death-event") {

    override fun readProperty(instance: EntityDeathEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "reviveHealth", "revive-health" -> instance.reviveHealth
            "droppedExp", "dropped-exp", "exp" -> instance.droppedExp
            "droppedItems", "dropped-items", "drops", "items" -> instance.drops
            "killer" -> instance.killer
            // TODO: sound
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: EntityDeathEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "droppedExp", "dropped-exp", "exp" -> instance.droppedExp = value.coerceInt()
            "reviveHealth", "revive-health" -> instance.reviveHealth = value.coerceDouble()
            // TODO: sound
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}