package cc.polarastrum.aiyatsbus.core

import cc.polarastrum.aiyatsbus.core.data.trigger.event.EventMapping
import cc.polarastrum.aiyatsbus.core.data.trigger.event.EventResolver
import org.bukkit.event.Event

/**
 * Aiyatsbus 事件执行器接口
 *
 * 负责处理附魔相关的事件触发和执行。
 * 管理事件监听器的注册、销毁和事件解析。
 *
 * @author mical
 * @since 2024/3/10 18:15
 */
interface AiyatsbusEventExecutor {

    /**
     * 注册监听器
     *
     * 注册所有事件监听器
     */
    fun registerListeners()

    /**
     * 注册一个监听器
     *
     * @param listen 监听器名称
     * @param eventMapping 事件映射
     */
    fun registerListener(listen: String, eventMapping: EventMapping)

    /**
     * 销毁监听器
     *
     * 销毁所有事件监听器
     */
    fun destroyListeners()

    /**
     * 销毁某个事件对应的监听器
     *
     * @param listen 监听器名称
     */
    fun destroyListener(listen: String)

    /**
     * 获取事件映射表
     *
     * @return 事件映射表
     */
    fun getEventMappings(): Map<String, EventMapping>

    /**
     * 获取外部事件映射表
     *
     * @return 外部事件映射表
     */
    fun getExternalEventMappings(): MutableMap<String, EventMapping>

    /**
     * 获取事件解析器
     *
     * @return 事件解析器映射表
     */
    fun getResolvers(): MutableMap<Class<out Event>, EventResolver<*>>

    /**
     * 获取事件解析器
     *
     * @param instance 事件实例
     * @return 对应的事件解析器
     */
    fun <T: Event> getResolver(instance: T): EventResolver<T>?

    /**
     * 重载事件映射
     *
     * 重新从 event-mapping.yml 读取映射，并重新注册事件监听器。
     */
    fun reloadEventMappings()
}
