package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.entity.EntityType
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity.PropertyEntityType
 *
 * @author mical
 * @since 2024/3/12 21:32
 */
@AiyatsbusProperty(
    id = "entity-type",
    bind = EntityType::class
)
class PropertyEntityType : AiyatsbusGenericProperty<EntityType>("entity-type") {

    override fun readProperty(instance: EntityType, key: String): OpenResult {
        val property: Any? = when (key) {
            "name" -> instance.name
            "namespacedKey", "namespaced-key" -> instance.key
            "translationKey", "translation-key" -> instance.translationKey()
            "isAlive", "is-alive", "alive" -> instance.isAlive
            "isSpawnable", "is-spawnable", "spawnable" -> instance.isSpawnable
            "ordinal" -> instance.ordinal
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: EntityType, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}