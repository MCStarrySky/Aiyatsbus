@file:Suppress("UNCHECKED_CAST")

package com.mcstarrysky.aiyatsbus.impl

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.data.CheckType
import com.mcstarrysky.aiyatsbus.core.data.trigger.event.EventMapping
import com.mcstarrysky.aiyatsbus.core.data.trigger.event.EventResolver
import com.mcstarrysky.aiyatsbus.core.event.AiyatsbusPrepareAnvilEvent
import com.mcstarrysky.aiyatsbus.core.util.Mirror
import com.mcstarrysky.aiyatsbus.core.util.Reloadable
import com.mcstarrysky.aiyatsbus.core.util.isNull
import com.mcstarrysky.aiyatsbus.core.util.mirrorNow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.conversion
import taboolib.platform.util.killer
import java.util.LinkedList
import java.util.function.BiFunction

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.DefaultAiyatsbusEventExecutor
 *
 * @author mical
 * @since 2024/7/18 00:45
 */
class DefaultAiyatsbusEventExecutor : AiyatsbusEventExecutor {

    override fun registerListeners() {
        TODO("Not yet implemented")
    }

    override fun destroyListeners() {
        TODO("Not yet implemented")
    }

    private fun processEvent(listen: String, event: Event, eventMapping: EventMapping, eventPriority: EventPriority) {
        val resolver = AiyatsbusEventExecutor.resolver[event::class.java] as? EventResolver<Event> ?: return
        /* 特殊事件处理 */
        resolver.eventResolver.accept(event)

        val entity = resolver.entityResolver.apply(event, eventMapping.playerReference)

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
            val item = resolver.itemResolver?.apply(event, eventMapping.itemReference, entity)
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

        @Config(value = "event-mapping.yml", autoReload = true)
        private lateinit var conf: Configuration

        @delegate:ConfigNode("mappings")
        val mappings: Map<String, EventMapping> by conversion<ConfigurationSection, Map<String, EventMapping>> {
            getKeys(false).associateWith { EventMapping(conf.getConfigurationSection(it)!!) }
        }

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

        @Awake(LifeCycle.ENABLE)
        fun reload() {
            registerLifeCycleTask(LifeCycle.ENABLE) {
                conf.onReload {
                    Aiyatsbus.api().getEventExecutor().destroyListeners()
                    Aiyatsbus.api().getEventExecutor().registerListeners()
                }
            }
        }
    }
}