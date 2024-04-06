package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveItemStack
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.util.Vector
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBlockDispenseEvent
 *
 * @author yanshiqwq
 * @since 2024/4/6 15:46
 */
@AiyatsbusProperty(
    id = "block-dispense-event",
    bind = BlockDispenseEvent::class
)
class PropertyBlockDispenseEvent : AiyatsbusGenericProperty<BlockDispenseEvent>("block-dispense-event"){

    override fun readProperty(instance: BlockDispenseEvent, key: String): OpenResult {
        val property: Any? = when(key) {
            "item" -> instance.item
            "velocity" -> instance.velocity
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockDispenseEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "item" -> instance.item = value?.liveItemStack ?: return OpenResult.successful()
            "velocity" -> instance.velocity = value as Vector? ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}