package com.mcstarrysky.aiyatsbus.impl

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.data.CheckType
import com.mcstarrysky.aiyatsbus.core.data.trigger.event.EventMapping
import com.mcstarrysky.aiyatsbus.core.data.trigger.event.EventResolver
import com.mcstarrysky.aiyatsbus.core.event.AiyatsbusPrepareAnvilEvent
import com.mcstarrysky.aiyatsbus.core.util.Mirror
import com.mcstarrysky.aiyatsbus.core.util.Reloadable
import com.mcstarrysky.aiyatsbus.core.util.inject.AwakePriority
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
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.common.platform.function.unregisterListener
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.conversion
import taboolib.platform.util.killer
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.DefaultAiyatsbusEventExecutor
 *
 * @author mical
 * @since 2024/7/18 00:45
 */
class DefaultAiyatsbusEventExecutor : AiyatsbusEventExecutor {

    private val resolvers = ConcurrentHashMap<Class<out Event>, EventResolver<*>>()

    private val externalMappings: ConcurrentHashMap<String, EventMapping> = ConcurrentHashMap()

    private val listeners: Table<String, EventPriority, ProxyListener> = HashBasedTable.create()

    private val cachedClasses = ConcurrentHashMap<String, Class<*>>()

    init {
        resolvers += PlayerEvent::class.java to EventResolver<PlayerEvent>({ event, _ -> event.player })
        resolvers += PlayerMoveEvent::class.java to EventResolver<PlayerMoveEvent>(
            entityResolver = { event, _ -> event.player },
            eventResolver = { event ->
                /* 过滤视角转动 */
                if (event.from.world == event.to.world && event.from.distance(event.to) < 1e-1) return@EventResolver
            }
        )
        resolvers += BlockPlaceEvent::class.java to EventResolver<BlockPlaceEvent>({ event, _ -> event.player })
        resolvers += BlockBreakEvent::class.java to EventResolver<BlockBreakEvent>({ event, _ -> event.player })
        resolvers += ProjectileHitEvent::class.java to EventResolver<ProjectileHitEvent>({ event, _ -> event.entity.shooter as? LivingEntity })
        resolvers += EntityDamageByEntityEvent::class.java to EventResolver<EntityDamageByEntityEvent>({ event, playerReference ->
            when (playerReference) {
                "damager", null -> when (event.damager) {
                    is Player -> event.damager as? LivingEntity
                    is Projectile -> ((event.damager as Projectile).shooter as? LivingEntity)
                    else -> null
                }
                "entity" -> event.entity as? LivingEntity
                else -> null
            }
        })
        resolvers += EntityDeathEvent::class.java to EventResolver<EntityDeathEvent>({ event, _ -> event.killer })
        resolvers += EntityEvent::class.java to EventResolver<EntityEvent>({ event, _ -> event.entity as? LivingEntity })
        resolvers += InventoryClickEvent::class.java to EventResolver<InventoryClickEvent>({ event, _ -> event.whoClicked })
        resolvers += InventoryEvent::class.java to EventResolver<InventoryEvent>({ event, _ -> event.view.player })
        resolvers += AiyatsbusPrepareAnvilEvent::class.java to EventResolver<AiyatsbusPrepareAnvilEvent>(
            entityResolver = { event, _ -> event.player },
            itemResolver = { event, itemReference, _ ->
                when (itemReference) {
                    "left" -> event.left
                    "right" -> event.right
                    "result" -> event.result
                    else -> event.getProperty(itemReference ?: return@EventResolver null, false) as? ItemStack ?: return@EventResolver null
                }
            }
        )
    }

    override fun registerListener(listen: String, eventMapping: EventMapping) {
        val (className, _, _, _, priority, ignoreCancelled) = eventMapping
        // 缓存
        val clazz = cachedClasses.computeIfAbsent(className) { Class.forName(className) }
        listeners.put(listen, priority, registerBukkitListener(clazz, priority, ignoreCancelled) {
            processEvent(listen, it as? Event ?: return@registerBukkitListener, eventMapping, priority)
        })
    }

    override fun registerListeners() {
        mappings.forEach(::registerListener)
        externalMappings.forEach(::registerListener)
    }

    override fun destroyListener(listen: String) {
        listeners.row(listen).values.forEach { unregisterListener(it) }
        listeners.row(listen).clear()
    }

    override fun destroyListeners() {
        listeners.values().forEach { unregisterListener(it) }
        listeners.clear()
    }

    override fun getEventMappings(): Map<String, EventMapping> {
        return mappings
    }

    override fun getExternalEventMappings(): MutableMap<String, EventMapping> {
        return externalMappings
    }

    override fun getResolvers(): MutableMap<Class<out Event>, EventResolver<*>> {
        return resolvers
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T: Event> getResolver(instance: T): EventResolver<T>? {
        var currentClass: Class<*>? = instance::class.java
        while (currentClass != null) {
            val resolver = resolvers[currentClass] as? EventResolver<T>
            if (resolver != null) {
                return resolver
            }
            currentClass = currentClass.superclass
        }
        return null
    }

    private fun processEvent(listen: String, event: Event, eventMapping: EventMapping, eventPriority: EventPriority) {
//        println("我是 $listen, 我的优先级是 ${eventPriority.name}")
        val resolver = getResolver(event) ?: return
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

        val enchants = fixedEnchants.entries.sortedBy { it.key.trigger.listenerPriority }

        for (enchantPair in enchants) {
            val enchant = enchantPair.key

            if (!enchant.limitations.checkAvailable(CheckType.USE, this, entity, slot, ignoreSlot).first) continue

            enchant.trigger.listeners
                .filterValues { it.listen == listen }
                .entries
                .sortedBy { it.value.priority }
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

        @Config(value = "core/event-mapping.yml", autoReload = true)
        private lateinit var conf: Configuration

        @delegate:ConfigNode("mappings", bind = "core/event-mapping.yml")
        private val mappings: MutableMap<String, EventMapping> by conversion<ConfigurationSection, MutableMap<String, EventMapping>> {
            getKeys(false).associateWith { EventMapping(conf.getConfigurationSection("mappings.$it")!!) }.toMutableMap()
        }

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusEventExecutor>(DefaultAiyatsbusEventExecutor())
        }

        @Reloadable
        @AwakePriority(LifeCycle.ENABLE, StandardPriorities.EVENT_EXECUTORS)
        fun onEnable() {
            Aiyatsbus.api().getEventExecutor().destroyListeners()
            Aiyatsbus.api().getEventExecutor().registerListeners()
        }

        @Awake(LifeCycle.ENABLE)
        fun onReload() {
            conf.onReload {
                Aiyatsbus.api().getEventExecutor().destroyListeners()
                Aiyatsbus.api().getEventExecutor().registerListeners()
            }
        }

        @Awake(LifeCycle.DISABLE)
        fun onDisable() {
            Aiyatsbus.api().getEventExecutor().destroyListeners()
        }
    }
}