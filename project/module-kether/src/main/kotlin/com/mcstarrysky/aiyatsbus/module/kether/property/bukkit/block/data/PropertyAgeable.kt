package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.block.data

import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.block.data.Ageable
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.block.data.PropertyAgeable
 *
 * @author mical
 * @since 2024/3/14 21:35
 */
@AiyatsbusProperty(
    id = "ageable",
    bind = Ageable::class
)
class PropertyAgeable : AiyatsbusGenericProperty<Ageable>("ageable") {

    override fun readProperty(instance: Ageable, key: String): OpenResult {
        val property: Any? = when (key) {
            "age" -> instance.age
            "maximumAge", "maximum-age", "maxAge", "max-age" -> instance.maximumAge
            else -> OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Ageable, key: String, value: Any?): OpenResult {
        when (key) {
            "age" -> {
                instance.age = value?.coerceInt() ?: return OpenResult.successful()
            }
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}