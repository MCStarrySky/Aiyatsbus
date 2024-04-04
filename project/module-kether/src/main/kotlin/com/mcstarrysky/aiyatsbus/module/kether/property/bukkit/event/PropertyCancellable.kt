package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event

import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.Cancellable
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.PropertyCancellable
 *
 * @author mical
 * @since 2024/3/10 13:24
 */
@AiyatsbusProperty(
    id = "cancellable",
    bind = Cancellable::class
)
class PropertyCancellable : AiyatsbusGenericProperty<Cancellable>("cancellable") {

    override fun readProperty(instance: Cancellable, key: String): OpenResult {
        val property: Any? = when (key) {
            "isCancelled", "is-cancelled", "cancelled", "cancel" -> instance.isCancelled
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Cancellable, key: String, value: Any?): OpenResult {
        when (key) {
            "isCancelled", "is-cancelled", "cancelled", "cancel" -> instance.isCancelled = value?.coerceBoolean() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}