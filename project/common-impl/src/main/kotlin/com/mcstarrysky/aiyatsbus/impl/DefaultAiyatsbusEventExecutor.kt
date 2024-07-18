package com.mcstarrysky.aiyatsbus.impl

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.data.CheckType
import com.mcstarrysky.aiyatsbus.core.data.trigger.event.EventMapping
import com.mcstarrysky.aiyatsbus.core.util.Mirror
import com.mcstarrysky.aiyatsbus.core.util.Reloadable
import com.mcstarrysky.aiyatsbus.core.util.isNull
import com.mcstarrysky.aiyatsbus.core.util.mirrorNow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.common.platform.function.unregisterListener

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.DefaultAiyatsbusEventExecutor
 *
 * @author mical
 * @since 2024/7/18 00:45
 */
class DefaultAiyatsbusEventExecutor : AiyatsbusEventExecutor {

    private val listeners: Table<String, EventPriority, ProxyListener> = HashBasedTable.create()

    override fun registerListener(listen: String, eventMapping: EventMapping) {
        val (clazz, _, _, _, eventPriorities) = eventMapping
        eventPriorities.forEach { priority ->
            listeners.put(listen, priority, registerBukkitListener(Class.forName(clazz), priority, true) {
                processEvent(listen, it as? Event ?: return@registerBukkitListener, eventMapping, priority)
            })
        }
    }

    override fun registerListeners() {
        AiyatsbusEventExecutor.mappings.forEach(::registerListener)
        AiyatsbusEventExecutor.externalMappings.forEach(::registerListener)
    }

    override fun destroyListener(listen: String) {
        listeners.row(listen).values.forEach { unregisterListener(it) }
        listeners.row(listen).clear()
    }

    override fun destroyListeners() {
        listeners.values().forEach { unregisterListener(it) }
        listeners.clear()
    }

    private fun processEvent(listen: String, event: Event, eventMapping: EventMapping, eventPriority: EventPriority) {
        val resolver = AiyatsbusEventExecutor.getResolver(event) ?: return
        /* 特殊事件处理 */
        resolver.eventResolver.apply(event)

        val entity = resolver.entityResolver.apply(event, eventMapping.playerReference) ?: return

        if (eventMapping.slots.isNotEmpty()) {
            eventMapping.slots.forEach { slot ->
                val item: ItemStack?
                try {
                    item = entity.equipment?.getItem(slot)
                } catch (_: Throwable) {
                    // 离谱的低版本报错:
                    // java.lang.NullPointerException: player.inventory.getItem(slot) must not be null
                    return@forEach
                }

                if (item.isNull) return@forEach

                item!!.triggerEts(listen, event, eventPriority, entity, slot, false)
            }
        } else {
            val item = resolver.itemResolver.apply(event, eventMapping.itemReference, entity)
            if (item.isNull) return
            item!!.triggerEts(listen, event, eventPriority, entity, null, true)
        }
    }

    private fun ItemStack.triggerEts(listen: String, event: Event, eventPriority: EventPriority, entity: LivingEntity, slot: EquipmentSlot?, ignoreSlot: Boolean = false) {
        for (enchantPair in fixedEnchants) {
            val enchant = enchantPair.key

            if (!enchant.limitations.checkAvailable(CheckType.USE, this, entity, slot, ignoreSlot).first) continue

            enchant.trigger.listeners
                .filterValues { it.priority == eventPriority && it.listen == listen }
                .forEach { (_, executor) ->
                    val vars = mutableMapOf(
                        "triggerSlot" to slot?.name,
                        "trigger-slot" to slot?.name,
                        "event" to event,
                        "player" to (entity as? Player ?: entity),
                        "item" to this,
                        "enchant" to enchant,
                        "level" to enchantPair.value,
                        "mirror" to Mirror.MirrorStatus()
                    )

                    vars += enchant.variables.variables(enchantPair.value, entity, this, false)

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

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusEventExecutor>(DefaultAiyatsbusEventExecutor())
        }

        @Reloadable
        @Awake(LifeCycle.CONST)
        fun load() {
            registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.EVENT_EXECUTORS) {
                Aiyatsbus.api().getEventExecutor().destroyListeners()
                Aiyatsbus.api().getEventExecutor().registerListeners()
            }
            registerLifeCycleTask(LifeCycle.DISABLE) {
                Aiyatsbus.api().getEventExecutor().destroyListeners()
            }
        }
    }
}