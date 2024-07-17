package com.mcstarrysky.aiyatsbus.core

import com.mcstarrysky.aiyatsbus.core.data.trigger.event.EventResolver
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerMoveEvent
import java.util.*
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

        val resolver = ConcurrentHashMap<Class<out Event>, EventResolver<out Event>>()

        init {
            resolver += PlayerEvent::class.java to EventResolver<PlayerEvent>({ event, _ -> event.player })
            resolver += PlayerMoveEvent::class.java to EventResolver<PlayerMoveEvent>(
                { event, _ -> event.player },
                { event ->
                    /* 过滤视角转动 */
                    if (event.from.world == event.to.world && event.from.distance(event.to) < 1e-1) return@EventResolver
                }
            )
        }
    }
}