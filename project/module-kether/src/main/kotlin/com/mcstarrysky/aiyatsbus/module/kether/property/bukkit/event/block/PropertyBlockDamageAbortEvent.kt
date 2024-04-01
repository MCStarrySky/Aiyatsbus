package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.block.BlockDamageAbortEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBlockDamageAbortEvent
 *
 * @author yanshiqwq
 * @since 2024/4/2 00:04
 */
@AiyatsbusProperty(
    id = "block-damage-abort-event",
    bind = BlockDamageAbortEvent::class
)
class PropertyBlockDamageAbortEvent : AiyatsbusGenericProperty<BlockDamageAbortEvent>("block-damage-abort-event") {
    override fun readProperty(instance: BlockDamageAbortEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "player" -> instance.player
            "itemInHand", "item-in-hand", "hand-item" -> instance.itemInHand
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockDamageAbortEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}