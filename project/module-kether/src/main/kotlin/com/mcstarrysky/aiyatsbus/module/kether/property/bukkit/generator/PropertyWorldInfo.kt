package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.generator

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.generator.WorldInfo
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.generator.PropertyWorldInfo
 *
 * @author yanshiqwq
 * @since 2024/4/1 00:35
 */
@AiyatsbusProperty(
    id = "world-info",
    bind = WorldInfo::class
)
class PropertyWorldInfo : AiyatsbusGenericProperty<WorldInfo>("world-info") {

    override fun readProperty(instance: WorldInfo, key: String): OpenResult {
        val property: Any? = when (key) {
            "name" -> instance.name
            "uid" -> instance.uid
            "environment" -> instance.environment
            "seed" -> instance.seed
            "minHeight", "min-height" -> instance.minHeight
            "maxHeight", "max-height" -> instance.maxHeight
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: WorldInfo, key: String, value: Any?): OpenResult {
        // TODO
        return OpenResult.failed()
    }
}
