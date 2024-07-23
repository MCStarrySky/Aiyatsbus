package com.mcstarrysky.aiyatsbus.core

import com.mcstarrysky.aiyatsbus.core.data.trigger.event.EventMapping
import com.mcstarrysky.aiyatsbus.core.data.trigger.event.EventResolver
import org.bukkit.event.Event

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
     * 注册一个监听器
     */
    fun registerListener(listen: String, eventMapping: EventMapping)

    /**
     * 销毁监听器
     */
    fun destroyListeners()

    /**
     * 销毁某个事件对应的监听器
     */
    fun destroyListener(listen: String)

    /**
     * 获取事件映射表
     */
    fun getEventMappings(): Map<String, EventMapping>

    /**
     * 获取外部事件映射表
     */
    fun getExternalEventMappings(): MutableMap<String, EventMapping>

    /**
     * 获取事件解析器
     */
    fun getResolvers(): MutableMap<Class<out Event>, EventResolver<*>>

    /**
     * 获取事件解析器
     */
    fun <T: Event> getResolver(instance: T): EventResolver<T>?
}