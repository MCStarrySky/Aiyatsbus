package com.mcstarrysky.aiyatsbus.core

import com.mcstarrysky.aiyatsbus.core.data.trigger.event.EventResolver
import com.mcstarrysky.aiyatsbus.core.event.AiyatsbusPrepareAnvilEvent
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
import org.bukkit.inventory.ItemStack
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.platform.util.killer
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusEventExecutor
 *
 * @author mical
 * @since 2024/3/10 18:15
 */
interface AiyatsbusEventExecutor {

    /**
     * 注册监听器
     */
    fun registerListeners()

    /**
     * 销毁监听器
     */
    fun destroyListeners()

    companion object {

        val resolver = ConcurrentHashMap<Class<out Event>, EventResolver<*>>()

        init {
            resolver += PlayerEvent::class.java to EventResolver<PlayerEvent>({ event, _ -> event.player })
            resolver += PlayerMoveEvent::class.java to EventResolver<PlayerMoveEvent>(
                entityResolver = { event, _ -> event.player },
                eventResolver = { event ->
                    /* 过滤视角转动 */
                    if (event.from.world == event.to.world && event.from.distance(event.to) < 1e-1) return@EventResolver
                }
            )
            resolver += BlockPlaceEvent::class.java to EventResolver<BlockPlaceEvent>({ event, _ -> event.player })
            resolver += BlockBreakEvent::class.java to EventResolver<BlockBreakEvent>({ event, _ -> event.player })
            resolver += ProjectileHitEvent::class.java to EventResolver<ProjectileHitEvent>({ event, _ -> event.entity.shooter as? LivingEntity })
            resolver += EntityDamageByEntityEvent::class.java to EventResolver<EntityDamageByEntityEvent>({ event, playerReference ->
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
            resolver += EntityDeathEvent::class.java to EventResolver<EntityDeathEvent>({ event, _ -> event.killer })
            resolver += EntityEvent::class.java to EventResolver<EntityEvent>({ event, _ -> event.entity as? LivingEntity })
            resolver += InventoryClickEvent::class.java to EventResolver<InventoryClickEvent>({ event, _ -> event.whoClicked })
            resolver += InventoryEvent::class.java to EventResolver<InventoryEvent>({ event, _ -> event.view.player })
            resolver += AiyatsbusPrepareAnvilEvent::class.java to EventResolver<AiyatsbusPrepareAnvilEvent>(
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

        @Suppress("UNCHECKED_CAST")
        fun <T: Event> getResolver(instance: T): EventResolver<T>? {
            var currentClass: Class<*>? = instance::class.java
            while (currentClass != null) {
                val resolver = resolver[currentClass] as? EventResolver<T>
                if (resolver != null) {
                    return resolver
                }
                currentClass = currentClass.superclass
            }
            return null
        }
    }
}