package com.mcstarrysky.aiyatsbus.module.custom.splendid

import com.mcstarrysky.aiyatsbus.core.fixedEnchants
import com.mcstarrysky.aiyatsbus.core.util.isNull
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.EventType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import taboolib.common.platform.event.EventPriority

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.custom.splendid.Utils
 *
 * @author mical
 * @since 2024/2/20 00:56
 */
fun EventType.triggerEts(
    event: Event,
    priority: EventPriority,
    slots: TriggerSlots,
    entity: LivingEntity
) {
    val inventory = entity.equipment ?: return
    slots.slots.forEach {
        val item = inventory.getItem(it)
        if (item.isNull) return@forEach

        item.fixedEnchants.forEach { enchantPair ->
            (enchantPair.key.trigger as? SplendidTrigger ?: return)
                .listeners
                .trigger(event, this, priority, entity, item, it, enchantPair.key, enchantPair.value)
        }
    }
}