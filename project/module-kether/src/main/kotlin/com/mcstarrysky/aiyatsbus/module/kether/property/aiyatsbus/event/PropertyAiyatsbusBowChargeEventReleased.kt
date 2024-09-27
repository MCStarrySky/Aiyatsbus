package com.mcstarrysky.aiyatsbus.module.kether.property.aiyatsbus.event

import com.mcstarrysky.aiyatsbus.core.event.AiyatsbusBowChargeEvent
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.aiyatsbus.event.PropertyAiyatsbusBowChargeEventPrepare
 *
 * @author mical
 * @since 2024/9/19 22:21
 */
@AiyatsbusProperty(
    id = "aiyatsbus-bow-charge-event-released",
    bind = AiyatsbusBowChargeEvent.Released::class
)
class PropertyAiyatsbusBowChargeEventReleased : AiyatsbusGenericProperty<AiyatsbusBowChargeEvent.Released>("aiyatsbus-bow-charge-event-released") {

    override fun readProperty(instance: AiyatsbusBowChargeEvent.Released, key: String): OpenResult {
        val property: Any? = when (key) {
            "player" -> instance.player
            "itemStack", "item-stack", "item" -> instance.itemStack
            "hand" -> instance.hand.name
            // TODO
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: AiyatsbusBowChargeEvent.Released, key: String, value: Any?): OpenResult {
        when (key) {
            // TODO
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}