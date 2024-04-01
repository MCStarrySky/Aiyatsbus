package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.block.BellRingEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBellRingEvent
 *
 * @author yanshiqwq
 * @since 2024/4/1 20:33
 */
@AiyatsbusProperty(
    id = "bell-ring-event",
    bind = BellRingEvent::class
)
class PropertyBellRingEvent : AiyatsbusGenericProperty<BellRingEvent>("bell-ring-event") {
    override fun readProperty(instance: BellRingEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "direction" -> instance.direction
            "entity" -> instance.entity
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BellRingEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}