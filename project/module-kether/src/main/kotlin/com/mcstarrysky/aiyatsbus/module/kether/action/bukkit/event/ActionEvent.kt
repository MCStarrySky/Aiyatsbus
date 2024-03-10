package com.mcstarrysky.aiyatsbus.module.kether.action.bukkit.event

import com.mcstarrysky.aiyatsbus.module.kether.action.ActionProvider
import org.bukkit.event.Event
import taboolib.common.Inject
import taboolib.common.OpenResult
import taboolib.module.kether.KetherProperty
import taboolib.module.kether.ScriptProperty

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.bukkit.event.ActionEvent
 *
 * @author mical
 * @since 2024/3/10 10:42
 */
@Inject
object ActionEvent : ActionProvider<Event>() {

    override fun read(instance: Event, key: String): Any {
        if (key.isEmpty()) return instance
        return when (key) {
            "name" -> OpenResult.successful(instance.eventName)
            else -> OpenResult.failed()
        }
    }

    override fun write(instance: Event, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }

    @KetherProperty(bind = Event::class)
    fun propertyEvent() = object : ScriptProperty<Event>("event.operator") {

        override fun read(instance: Event, key: String): OpenResult {
            // 如果获取失败应该是直接返回 OpenResult.failed(), 所以这里直接用 successful 包装
            return ActionEvent.read(instance, key)
                .let { if (it !is OpenResult) OpenResult.successful(it) else it }
        }

        override fun write(instance: Event, key: String, value: Any?): OpenResult {
            return ActionEvent.write(instance, key, value)
        }
    }
}