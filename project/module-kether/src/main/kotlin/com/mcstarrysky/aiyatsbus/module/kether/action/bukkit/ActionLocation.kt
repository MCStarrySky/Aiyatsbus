package com.mcstarrysky.aiyatsbus.module.kether.action.bukkit

import com.mcstarrysky.aiyatsbus.module.kether.action.ActionProvider
import org.bukkit.Location
import taboolib.common.Inject
import taboolib.common.OpenResult
import taboolib.module.kether.KetherProperty
import taboolib.module.kether.ScriptProperty

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.bukkit.ActionLocation
 *
 * @author mical
 * @since 2024/3/10 11:11
 */
@Inject
object ActionLocation : ActionProvider<Location>() {

    override fun read(instance: Location, key: String): Any {
        if (key.isEmpty()) return instance
        return when (key) {
            "x" -> OpenResult.successful(instance.x)
            "y" -> OpenResult.successful(instance.y)
            "z" -> OpenResult.successful(instance.z)
            "yaw" -> OpenResult.successful(instance.yaw)
            "pitch" -> OpenResult.successful(instance.pitch)
            "world" -> OpenResult.successful(instance.world)
            else -> OpenResult.failed()
        }
    }

    override fun write(instance: Location, key: String, value: Any?): OpenResult {
        TODO("Not yet implemented")
    }

    @KetherProperty(bind = Location::class)
    fun propertyLocation() = object : ScriptProperty<Location>("location.operator") {

        override fun read(instance: Location, key: String): OpenResult {
            return ActionLocation.read(instance, key)
                .let { if (it !is OpenResult) OpenResult.successful(it) else it }
        }

        override fun write(instance: Location, key: String, value: Any?): OpenResult {
            return OpenResult.failed()
        }
    }
}