package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block

import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.block.BlockExpEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBlockExpEvent
 *
 * @author mical
 * @since 2024/3/10 13:33
 */
@AiyatsbusProperty(
    id = "block-exp-event",
    bind = BlockExpEvent::class
)
class PropertyBlockExpEvent : AiyatsbusGenericProperty<BlockExpEvent>("block-exp-event") {

    override fun readProperty(instance: BlockExpEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "expToDrop", "exp-to-drop", "exp" -> instance.expToDrop
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockExpEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "expToDrop", "exp-to-drop", "exp" -> instance.expToDrop = value?.coerceInt() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}