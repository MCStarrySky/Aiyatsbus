package com.mcstarrysky.aiyatsbus.core.data.trigger.event

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import com.mcstarrysky.aiyatsbus.core.data.CheckType
import com.mcstarrysky.aiyatsbus.core.fixedEnchants
import com.mcstarrysky.aiyatsbus.core.util.Mirror
import com.mcstarrysky.aiyatsbus.core.util.mirrorNow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.warning
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.kether.LocalizedException

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.trigger.EventExecutor
 *
 * @author mical
 * @since 2024/3/9 18:35
 */
data class EventExecutor(
    private val root: ConfigurationSection,
    private val enchant: AiyatsbusEnchantment,
    val listen: String = root.getString("listen")!!,
    val handle: String = root.getString("handle") ?: "",
    val priority: EventPriority = EventPriority.valueOf(root.getString("priority") ?: "HIGHEST"),
) {

    init {
        if (AiyatsbusSettings.enableKetherPreheat) {
            try {
                Aiyatsbus.api().getKetherHandler().preheat(handle)
            } catch (ex: LocalizedException) {
                warning("Unable to preheat the event executor ${root.name} of enchantment ${enchant.id}: $ex")
            }
        }
    }

    fun triggerEts(item: ItemStack, listen: String, event: Event, eventPriority: EventPriority, entity: LivingEntity, slot: EquipmentSlot?, ignoreSlot: Boolean = false) {
        for (enchantPair in item.fixedEnchants) {
            val enchant = enchantPair.key

            if (!enchant.limitations.checkAvailable(CheckType.USE, item, entity, slot, ignoreSlot).first) continue

            enchant.trigger.listeners
                .filterValues { it.priority == eventPriority && it.listen == listen }
                .forEach { (_, executor) ->
                    val vars = mutableMapOf(
                        "triggerSlot" to slot?.name,
                        "trigger-slot" to slot?.name,
                        "event" to event,
                        "player" to (entity as? Player ?: entity),
                        "item" to item,
                        "enchant" to enchant,
                        "level" to enchantPair.value,
                        "mirror" to Mirror.MirrorStatus()
                    )

                    vars += enchant.variables.variables(enchantPair.value, entity, item, false)

                    if (AiyatsbusSettings.enablePerformanceTool) {
                        mirrorNow("Enchantment:Listener:Kether" + if (AiyatsbusSettings.showPerformanceDetails) ":${enchant.basicData.id}" else "") {
                            vars += "mirror" to it
                            Aiyatsbus.api().getKetherHandler().invoke(executor.handle, entity, variables = vars)
                        }
                    } else {
                        Aiyatsbus.api().getKetherHandler().invoke(executor.handle, entity, variables = vars)
                    }
                }
        }
    }
}