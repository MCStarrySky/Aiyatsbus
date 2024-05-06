package com.mcstarrysky.aiyatsbus.module.kether.property.aiyatsbus.event

import com.mcstarrysky.aiyatsbus.core.event.AiyatsbusPrepareAnvilEvent
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveItemStack
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.aiyatsbus.event.PropertyAiyatsbusPrepareAnvilEvent
 *
 * @author mical
 * @since 2024/5/6 22:00
 */
@AiyatsbusProperty(
    id = "aiyatsbus-prepare-anvil-event",
    bind = AiyatsbusPrepareAnvilEvent::class
)
class PropertyAiyatsbusPrepareAnvilEvent : AiyatsbusGenericProperty<AiyatsbusPrepareAnvilEvent>("aiyatsbus-prepare-anvil-event") {

    override fun readProperty(instance: AiyatsbusPrepareAnvilEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "player" -> instance.player
            "left" -> instance.left
            "right" -> instance.right
            "result" -> instance.result
            "name" -> instance.name
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: AiyatsbusPrepareAnvilEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "result" -> instance.result = value?.liveItemStack
            "name" -> instance.name = value?.toString()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}