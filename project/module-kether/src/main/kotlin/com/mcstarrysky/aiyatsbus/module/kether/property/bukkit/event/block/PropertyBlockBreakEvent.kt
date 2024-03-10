package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block

import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.module.kether.property.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.property.AiyatsbusProperty
import org.bukkit.event.block.BlockBreakEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBlockBreakEvent
 *
 * @author mical
 * @since 2024/3/10 13:35
 */
@AiyatsbusProperty(
    id = "block-break-event",
    bind = BlockBreakEvent::class
)
class PropertyBlockBreakEvent : AiyatsbusGenericProperty<BlockBreakEvent>("block-break-event") {

    override fun readProperty(instance: BlockBreakEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "player" -> instance.player
            "isDropItems", "is-drop-items", "drop-items" -> instance.isDropItems
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockBreakEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "isDropItems", "is-drop-items", "drop-items" -> {
                instance.isDropItems = value?.coerceBoolean() ?: return OpenResult.successful()
            }
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}