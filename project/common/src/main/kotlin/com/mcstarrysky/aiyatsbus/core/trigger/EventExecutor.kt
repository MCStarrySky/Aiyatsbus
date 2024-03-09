package com.mcstarrysky.aiyatsbus.core.trigger

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.StandardPriorities
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
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.conversion

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.trigger.EventExecutor
 *
 * @author mical
 * @since 2024/3/9 18:35
 */
data class EventExecutor(
    val listen: String,
    val handle: String
) {

    private val playerReference: String? = "player"

    private val priority: String? = "NORMAL"
    val ignoreCancelled: Boolean? = false

    @Transient
    val eventPriority = EventPriority.valueOf(priority ?: "NORMAL")

    @Transient
    private lateinit var proxyListener: ProxyListener

    fun registerListener() {
        val listen = mappings[listen] ?: listen
        registerBukkitListener(Class.forName(listen), eventPriority, ignoreCancelled ?: false) {
            val event = it as? Event ?: return@registerBukkitListener
            /* 特殊事件处理 */
            when (event) {
                is PlayerMoveEvent -> {
                    /* 过滤视角转动 */
                    if (event.from.world == event.to?.world && event.from.distance(event.to ?: return@registerBukkitListener) < 1e-1) return@registerBukkitListener
                }
            }

            val player = when (event) {
                is PlayerEvent -> event.player
                is BlockBreakEvent -> event.player
                is BlockPlaceEvent -> event.player
                is EntityDamageByEntityEvent -> {
                    when (event.damager) {
                        is Player -> event.damager
                        is Projectile -> ((event.damager as Projectile).shooter as? Player)
                        else -> null
                    }
                }
                is EntityEvent -> (event.entity as? Player)
                is InventoryClickEvent -> event.whoClicked as? Player
                is InventoryEvent -> event.view.player as? Player
                else -> {
                    playerReference?.let { ref ->
                        try {
                            event.getProperty<Any>(ref, false) as? Player
                        } catch (ignored: Exception) {
                            null
                        }
                    }
                }
            }

            Aiyatsbus.api().getKetherHandler().invoke(handle, player as? Player)
        }
    }

    fun unregisterListener() {
        if (!::proxyListener.isInitialized) return
        taboolib.common.platform.function.unregisterListener(proxyListener)
    }

    companion object {

        @Config("event-mapping.yml", autoReload = true)
        lateinit var conf: Configuration
            private set

        @delegate:ConfigNode("mappings", bind = "event-mapping.yml")
        val mappings: Map<String, String> by conversion<ConfigurationSection, Map<String, String>> {
            getValues(false).mapValues { it.value.toString() }
        }

        @Awake(LifeCycle.CONST)
        fun init() {
            registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.EVENT_EXECUTORS) {
                reload()
            }
        }

        @Awake(LifeCycle.ENABLE)
        private fun autoReload() {
            conf.onReload {
                reload()
            }
        }

        private fun reload() {
            Aiyatsbus.api().getEnchantmentManager().getByNames().values
                .let {
                    it.map { ench -> ench.trigger.listeners.values }.flatten().forEach { listener -> listener.unregisterListener() }
                    it.forEach { ench -> ench.trigger.loadListeners() }
                }
        }

        fun load(section: ConfigurationSection): EventExecutor {
            return EventExecutor(section["listen"].toString(), section["handle"].toString())
        }
    }
}