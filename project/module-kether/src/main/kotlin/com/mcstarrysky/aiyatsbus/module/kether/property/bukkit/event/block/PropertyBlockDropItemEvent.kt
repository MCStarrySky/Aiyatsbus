package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.block.BlockDropItemEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBlockDropItemEvent
 *
 * @author yanshiqwq
 * @since 2024/4/6 15:55
 */
@AiyatsbusProperty(
    id = "block-drop-item-event",
    bind = BlockDropItemEvent::class
)
class PropertyBlockDropItemEvent : AiyatsbusGenericProperty<BlockDropItemEvent>("block-drop-item-event"){

    override fun readProperty(instance: BlockDropItemEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "player" -> instance.player
            "blockState", "block-state", "state" -> instance.blockState
            "items", "item" -> instance.items
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockDropItemEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}