package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block

import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.block.BlockDamageEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBlockDamageEvent
 *
 * @author yanshiqwq
 * @since 2024/4/2 00:15
 */

@AiyatsbusProperty(
    id = "block-damage-event",
    bind = BlockDamageEvent::class
)
class PropertyBlockDamageEvent : AiyatsbusGenericProperty<BlockDamageEvent>("block-damage-event"){
    override fun readProperty(instance: BlockDamageEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "player" -> instance.player
            "instaBreak", "insta-break", "instant-break" -> instance.instaBreak
            "itemInHand", "item-in-hand", "hand-item" -> instance.itemInHand
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockDamageEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "instaBreak", "insta-break", "instant-break" -> instance.instaBreak = value?.coerceBoolean() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}