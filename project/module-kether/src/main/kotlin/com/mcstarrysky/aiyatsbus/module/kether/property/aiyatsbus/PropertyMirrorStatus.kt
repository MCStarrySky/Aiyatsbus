package com.mcstarrysky.aiyatsbus.module.kether.property.aiyatsbus

import com.mcstarrysky.aiyatsbus.core.util.Mirror
import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.aiyatsbus.PropertyMirror
 *
 * @author mical
 * @since 2024/3/14 23:23
 */
@AiyatsbusProperty(
    id = "mirror-status",
    bind = Mirror.MirrorStatus::class
)
class PropertyMirrorStatus : AiyatsbusGenericProperty<Mirror.MirrorStatus>("mirror-status") {

    override fun readProperty(instance: Mirror.MirrorStatus, key: String): OpenResult {
        val property: Any? = when (key) {
            "isCancelled", "is-cancelled", "cancelled", "cancel" -> instance.isCancelled
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Mirror.MirrorStatus, key: String, value: Any?): OpenResult {
        when (key) {
            "isCancelled", "is-cancelled", "cancelled", "cancel" -> {
                instance.isCancelled = value?.coerceBoolean() ?: return OpenResult.successful()
            }
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}