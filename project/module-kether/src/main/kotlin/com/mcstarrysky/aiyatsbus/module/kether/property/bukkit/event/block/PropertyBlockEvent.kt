package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.block.BlockEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBlockEvent
 *
 * @author mical
 * @since 2024/3/10 13:28
 */
@AiyatsbusProperty(
    id = "block-event",
    bind = BlockEvent::class
)
class PropertyBlockEvent : AiyatsbusGenericProperty<BlockEvent>("block-event") {

    override fun readProperty(instance: BlockEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "block" -> instance.block
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}