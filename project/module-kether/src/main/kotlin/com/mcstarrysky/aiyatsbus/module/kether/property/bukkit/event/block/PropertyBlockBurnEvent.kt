package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.block.BlockBurnEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBlockBurnEvent
 *
 * @author yanshiqwq
 * @since 2024/4/1 20:38
 */
@AiyatsbusProperty(
    id = "block-burn-event",
    bind = BlockBurnEvent::class
)
class PropertyBlockBurnEvent : AiyatsbusGenericProperty<BlockBurnEvent>("block-burn-event"){
    override fun readProperty(instance: BlockBurnEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "ignitingBlock", "igniting-block" -> instance.ignitingBlock
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockBurnEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}