package com.mcstarrysky.aiyatsbus.impl

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEventExecutor
import com.mcstarrysky.aiyatsbus.core.StandardPriorities
import com.mcstarrysky.aiyatsbus.core.fixedEnchants
import com.mcstarrysky.aiyatsbus.core.util.Reloadable
import com.mcstarrysky.aiyatsbus.core.util.isNull
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerMoveEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.common.platform.function.unregisterListener
import taboolib.library.reflex.Reflex.Companion.getProperty

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.DefaultAiyatsbusEventExecutor
 *
 * @author mical
 * @since 2024/3/10 18:18
 */
class DefaultAiyatsbusEventExecutor : AiyatsbusEventExecutor {

    private val listeners: Table<String, EventPriority, ProxyListener> = HashBasedTable.create()

    override fun registerListeners() {
        AiyatsbusEventExecutor.mappings.forEach { (listen, clazz) ->
            AiyatsbusEventExecutor.eventPriorities[listen]?.forEach { priority ->
                listeners.put(listen, priority, registerBukkitListener(Class.forName(clazz), priority, true) {
                    handle(it, listen, priority)
                })
            }
        }
    }

    private fun handle(it: Any?, listen: String, eventPriority: EventPriority) {
        val playerReference: String? = AiyatsbusEventExecutor.playerReferences[listen]
        val slot = AiyatsbusEventExecutor.slots[listen] ?: return

        val event = it as? Event ?: return
        /* 特殊事件处理 */
        when (event) {
            is PlayerMoveEvent -> {
                /* 过滤视角转动 */
                if (event.from.world == event.to.world && event.from.distance(event.to) < 1e-1) return
            }
        }

        val player = when (event) {
            is PlayerEvent -> event.player
            is BlockBreakEvent -> event.player
            is BlockPlaceEvent -> event.player
            is EntityDamageByEntityEvent -> {
                when (event.damager) {
                    is Player -> event.damager
                    is Projectile -> ((event.damager as Projectile).shooter as? LivingEntity)
                    else -> null
                }
            }
            is EntityEvent -> (event.entity as? LivingEntity)
            is InventoryClickEvent -> event.whoClicked
            is InventoryEvent -> event.view.player
            else -> {
                playerReference?.let { ref ->
                    try {
                        event.getProperty<Any>(ref, false) as? LivingEntity
                    } catch (ignored: Exception) {
                        null
                    }
                }
            }
        }

        val entity = player as? LivingEntity ?: return
        val inventory = entity.equipment ?: return
        slot.slots.forEach {
            val item = inventory.getItem(it)
            if (item.isNull) return@forEach

            item.fixedEnchants.forEach { enchantPair ->
                val enchant = enchantPair.key
                enchant.trigger.listeners
                    .filterValues { it.getEventPriority() == eventPriority && it.listen == listen }
                    .forEach { (_, executor) ->
                        executor.baffle?.let { baffle ->
                            val key = (entity as? LivingEntity)?.uniqueId?.toString() ?: event.eventName
                            if (!baffle.hasNext(key)) return
                        }

                        Aiyatsbus.api().getKetherHandler().invoke(executor.handle, player as? Player, variables = mapOf(
                            "@Event" to event,
                            "event" to event,
                            "@Player" to player,
                            "player" to player,
                            "playerName" to player.name,
                            "@Item" to item,
                            "item" to item,
                            "@Enchant" to enchant,
                            "enchant" to enchant,
                            "@Level" to enchantPair.value,
                            "level" to enchantPair.value
                        ))
                    }
            }
        }
    }

    override fun unregisterListeners() {
        listeners.values().forEach { unregisterListener(it) }
        listeners.clear()
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
                Aiyatsbus.api().getEventExecutor().unregisterListeners()
                Aiyatsbus.api().getEventExecutor().registerListeners()
                AiyatsbusEventExecutor.conf.onReload {
                    Aiyatsbus.api().getEventExecutor().unregisterListeners()
                    Aiyatsbus.api().getEventExecutor().registerListeners()
                }
            }
        }

        @Awake(LifeCycle.DISABLE)
        fun unload() {
            Aiyatsbus.api().getEventExecutor().unregisterListeners()
        }
    }
}