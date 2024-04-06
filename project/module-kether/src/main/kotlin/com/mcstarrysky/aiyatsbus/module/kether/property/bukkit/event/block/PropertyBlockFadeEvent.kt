package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.block.BlockFadeEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBlockFadeEvent
 *
 * @author yanshiqwq
 * @since 2024/4/6 16:10
 */
@AiyatsbusProperty(
    id = "block-fade-event",
    bind = BlockFadeEvent::class
)
class PropertyBlockFadeEvent : AiyatsbusGenericProperty<BlockFadeEvent>("block-fade-event"){

    override fun readProperty(instance: BlockFadeEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "newState", "state" -> instance.newState
            else -> OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockFadeEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}