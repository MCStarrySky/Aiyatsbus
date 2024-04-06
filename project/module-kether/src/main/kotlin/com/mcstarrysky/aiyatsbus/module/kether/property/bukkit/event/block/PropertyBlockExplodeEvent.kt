package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block

import com.mcstarrysky.aiyatsbus.core.util.coerceFloat
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.block.BlockExplodeEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBlockExplodeEvent
 *
 * @author yanshiqwq
 * @since 2024/4/6 16:06
 */
@AiyatsbusProperty(
    id = "block-explode-event",
    bind = BlockExplodeEvent::class
)
class PropertyBlockExplodeEvent : AiyatsbusGenericProperty<BlockExplodeEvent>("block-explode-event"){

    override fun readProperty(instance: BlockExplodeEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "blockList", "blocks", "list" -> instance.blockList()
            "yield" -> instance.yield
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockExplodeEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "yield" -> instance.yield = value?.coerceFloat() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}