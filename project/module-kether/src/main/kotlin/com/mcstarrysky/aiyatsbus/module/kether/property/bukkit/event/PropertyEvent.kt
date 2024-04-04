package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.Event
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.PropertyEvent
 *
 * @author mical
 * @since 2024/3/10 13:26
 */
@AiyatsbusProperty(
    id = "event",
    bind = Event::class
)
class PropertyEvent : AiyatsbusGenericProperty<Event>("event") {

    override fun readProperty(instance: Event, key: String): OpenResult {
        val property: Any? = when (key) {
            "eventName", "event-name", "name" -> instance.eventName
            "isAsynchronous", "is-asynchronous", "asynchronous", "is-async", "async" -> instance.isAsynchronous
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Event, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}