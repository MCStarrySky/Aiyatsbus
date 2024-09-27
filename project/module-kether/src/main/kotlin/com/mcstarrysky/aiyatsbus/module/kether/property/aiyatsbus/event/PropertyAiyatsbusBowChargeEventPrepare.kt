package com.mcstarrysky.aiyatsbus.module.kether.property.aiyatsbus.event

import com.mcstarrysky.aiyatsbus.core.event.AiyatsbusBowChargeEvent
import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
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
    id = "aiyatsbus-bow-charge-event-prepare",
    bind = AiyatsbusBowChargeEvent.Prepare::class
)
class PropertyAiyatsbusBowChargeEventPrepare : AiyatsbusGenericProperty<AiyatsbusBowChargeEvent.Prepare>("aiyatsbus-bow-charge-event-prepare") {

    override fun readProperty(instance: AiyatsbusBowChargeEvent.Prepare, key: String): OpenResult {
        val property: Any? = when (key) {
            "player" -> instance.player
            "itemStack", "item-stack", "item" -> instance.itemStack
            "hand" -> instance.hand.name
            "isAllowed", "is-allowed", "allowed" -> instance.isAllowed
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: AiyatsbusBowChargeEvent.Prepare, key: String, value: Any?): OpenResult {
        when (key) {
            "isAllowed", "is-allowed", "allowed" -> instance.isAllowed = value?.coerceBoolean() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}