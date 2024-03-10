package com.mcstarrysky.aiyatsbus.module.kether.action.bukkit.event

import com.mcstarrysky.aiyatsbus.module.kether.action.ActionProvider
import org.bukkit.event.Cancellable
import taboolib.common.Inject
import taboolib.common.OpenResult
import taboolib.module.kether.KetherProperty
import taboolib.module.kether.ScriptProperty

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.bukkit.event.ActionCancellable
 *
 * @author mical
 * @since 2024/3/10 12:51
 */
@Inject
object ActionCancellable : ActionProvider<Cancellable>() {

    override fun read(instance: Cancellable, key: String): Any {
        if (key.isEmpty()) return OpenResult.successful(instance.isCancelled)
        return when (key) {
            "cancelled" -> OpenResult.successful(instance.isCancelled)
            else -> OpenResult.failed()
        }
    }

    override fun write(instance: Cancellable, key: String, value: Any?): OpenResult {
        return when (key) {
            "cancelled" -> {
                instance.isCancelled = value as Boolean
                OpenResult.successful()
            }
            else -> OpenResult.failed()
        }
    }

    @KetherProperty(bind = Cancellable::class)
    fun propertyCancellable() = object : ScriptProperty<Cancellable>("cancellable.operator") {

        override fun read(instance: Cancellable, key: String): OpenResult {
            return ActionCancellable.read(instance, key)
                .let { if (it !is OpenResult) OpenResult.successful(it) else it }
        }

        override fun write(instance: Cancellable, key: String, value: Any?): OpenResult {
            return ActionCancellable.write(instance, key, value)
        }
    }
}