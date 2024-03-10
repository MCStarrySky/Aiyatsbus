package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit

import com.mcstarrysky.aiyatsbus.module.kether.property.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.property.AiyatsbusProperty
import org.bukkit.World
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.PropertyWorld
 *
 * @author mical
 * @since 2024/3/10 13:20
 */
@AiyatsbusProperty(
    id = "world",
    bind = World::class
)
class PropertyWorld : AiyatsbusGenericProperty<World>("world") {

    override fun readProperty(instance: World, key: String): OpenResult {
        val property: Any? = when (key) {
            "name" -> instance.name
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: World, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}