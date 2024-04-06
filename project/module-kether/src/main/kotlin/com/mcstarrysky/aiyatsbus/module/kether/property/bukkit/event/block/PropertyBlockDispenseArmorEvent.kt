package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.block.BlockDispenseArmorEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBlockDispenseArmorEvent
 *
 * @author yanshiqwq
 * @since 2024/4/6 15:43
 */
@AiyatsbusProperty(
    id = "block-dispense-armor-event",
    bind = BlockDispenseArmorEvent::class
)
class PropertyBlockDispenseArmorEvent : AiyatsbusGenericProperty<BlockDispenseArmorEvent>("block-dispense-armor-event") {

    override fun readProperty(instance: BlockDispenseArmorEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "targetEntity", "target-entity", "target", "entity" -> instance.targetEntity
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockDispenseArmorEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}