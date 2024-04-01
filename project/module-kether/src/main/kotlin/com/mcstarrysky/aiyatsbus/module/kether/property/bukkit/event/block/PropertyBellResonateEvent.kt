package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.block.BellResonateEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBellResonateEvent
 *
 * @author yanshiqwq
 * @since 2024/4/1 20:29
 */
@AiyatsbusProperty(
    id = "bell-resonate-event",
    bind = BellResonateEvent::class
)
class PropertyBellResonateEvent : AiyatsbusGenericProperty<BellResonateEvent>("bell-resonate-event") {

    override fun readProperty(instance: BellResonateEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "resonatedEntities", "resonated-entities" -> instance.resonatedEntities
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BellResonateEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}