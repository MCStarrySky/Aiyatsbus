package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit

import com.mcstarrysky.aiyatsbus.core.util.coerceDouble
import com.mcstarrysky.aiyatsbus.core.util.coerceFloat
import com.mcstarrysky.aiyatsbus.core.util.serialized
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector
import taboolib.common.OpenResult
import taboolib.platform.util.toProxyLocation

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.PropertyBukkitLocation
 *
 * @author mical
 * @since 2024/3/10 13:18
 */
@AiyatsbusProperty(
    id = "location-bukkit",
    bind = Location::class
)
class PropertyLocation : AiyatsbusGenericProperty<Location>("location-bukkit") {

    override fun readProperty(instance: Location, key: String): OpenResult {
        val property: Any? = when (key) {
            "clone" -> instance.clone()
            "taboo" -> instance.toProxyLocation()
            "block" -> instance.block
            "blockX", "block-x" -> instance.blockX
            "blockY", "block-y" -> instance.blockY
            "blockZ", "block-z" -> instance.blockZ
            "chunk" -> instance.chunk
            "direction" -> instance.direction
            "pitch" -> instance.pitch
            "world" -> instance.world
            "x" -> instance.x
            "y" -> instance.y
            "yaw" -> instance.yaw
            "z" -> instance.z
            "isWorldLoaded", "world-loaded", "loaded" -> instance.isWorldLoaded
            "length" -> instance.length()
            "lengthSquared", "length-squared", "squared", "sq" -> instance.lengthSquared()
            "serialize" -> instance.serialize()
            "serialized" -> instance.serialized
            "toVector", "vector" -> instance.toVector()
            "toString", "string" -> instance.toString()
            "zero" -> instance.zero()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Location, key: String, value: Any?): OpenResult {
        when (key) {
            "direction" -> {
                instance.direction = value as? Vector ?: return OpenResult.successful()
            }
            "pitch" -> {
                instance.pitch = value?.coerceFloat(0f) ?: return OpenResult.successful()
            }
            "world" -> {
                val world = when (value) {
                    is World -> value
                    is String -> Bukkit.getWorld(value)
                    is Location -> value.world
                    else -> null
                }
                instance.world = world ?: return OpenResult.successful()
            }
            "x" -> {
                instance.x = value?.coerceDouble() ?: return OpenResult.successful()
            }
            "y" -> {
                instance.y = value?.coerceDouble() ?: return OpenResult.successful()
            }
            "yaw" -> {
                instance.yaw = value?.coerceFloat() ?: return OpenResult.successful()
            }
            "z" -> {
                instance.z = value?.coerceDouble() ?: return OpenResult.successful()
            }
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}