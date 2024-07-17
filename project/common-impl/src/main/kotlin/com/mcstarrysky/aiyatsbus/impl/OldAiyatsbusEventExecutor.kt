package com.mcstarrysky.aiyatsbus.impl

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.data.CheckType
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
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.*
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.platform.util.killer

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.DefaultAiyatsbusEventExecutor
 *
 * @author mical
 * @since 2024/3/10 18:18
 */
class OldAiyatsbusEventExecutor : AiyatsbusEventExecutor {

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
        val slot = AiyatsbusEventExecutor.slots[listen]

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
            is ProjectileHitEvent -> event.entity.shooter as? LivingEntity
            is EntityDamageByEntityEvent -> {
                when (playerReference) {
                    "damager", null -> when (event.damager) {
                        is Player -> event.damager
                        is Projectile -> ((event.damager as Projectile).shooter as? LivingEntity)
                        else -> null
                    }
                    "entity" -> event.entity as? LivingEntity
                    else -> null
                }
            }

            is EntityDeathEvent -> event.killer
            is EntityEvent -> (event.entity as? LivingEntity)
            is InventoryClickEvent -> event.whoClicked
            is InventoryEvent -> event.view.player

            is AiyatsbusPrepareAnvilEvent -> event.player
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

        if (slot == null) {
            val itemReference = AiyatsbusEventExecutor.itemReferences[listen]
            // 如果没有指定 slot, 但 itemReference 不为空, 则获取 itemReference
            val item = when (event) {
                is AiyatsbusPrepareAnvilEvent -> {
                    when (itemReference) {
                        "left" -> event.left
                        "right" -> event.right
                        "result" -> event.result
                        else -> event.getProperty(itemReference ?: return, false) as? ItemStack ?: return
                    }
                }
                else -> event.getProperty(itemReference ?: return, false) as? ItemStack ?: return
            }
            if (item.isNull) return

            item!!.triggerEts(listen, event, eventPriority, entity, null, true)
            return
        }

        slot.slots.forEach {
            val item: ItemStack?
            try {
                item = inventory.getItem(it ?: return)
            } catch (_: Throwable) {
                // 离谱的低版本报错:
                // java.lang.NullPointerException: player.inventory.getItem(slot) must not be null
                return@forEach
            }
            if (item.isNull) return@forEach

            item.triggerEts(listen, event, eventPriority, entity, it, false)
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

    override fun destroyListeners() {
        listeners.values().forEach { unregisterListener(it) }
        listeners.clear()
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusEventExecutor>(OldAiyatsbusEventExecutor())
        }

        @Reloadable
        @Awake(LifeCycle.CONST)
        fun load() {
            registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.EVENT_EXECUTORS) {
                Aiyatsbus.api().getEventExecutor().destroyListeners()
                Aiyatsbus.api().getEventExecutor().registerListeners()
            }
        }

        @Awake(LifeCycle.ENABLE)
        fun reload() {
            registerLifeCycleTask(LifeCycle.ENABLE) {
                AiyatsbusEventExecutor.conf.onReload {
                    Aiyatsbus.api().getEventExecutor().destroyListeners()
                    Aiyatsbus.api().getEventExecutor().registerListeners()
                }
            }
        }

        @Awake(LifeCycle.DISABLE)
        fun unload() {
            Aiyatsbus.api().getEventExecutor().destroyListeners()
        }
    }
}