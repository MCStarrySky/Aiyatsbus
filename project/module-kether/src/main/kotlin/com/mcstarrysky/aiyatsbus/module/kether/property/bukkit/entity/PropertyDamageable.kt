package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity

import com.mcstarrysky.aiyatsbus.core.util.coerceDouble
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.entity.Damageable
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity.PropertyDamageable
 *
 * @author yanshiqwq
 * @since 2024/4/4 12:03
 */
@AiyatsbusProperty(
    id = "damageable",
    bind = Damageable::class
)
class PropertyDamageable : AiyatsbusGenericProperty<Damageable>("damageable") {

    override fun readProperty(instance: Damageable, key: String): OpenResult {
        val property: Any? = when (key) {
            "health" -> instance.health
            "absorptionAmount", "absorption-amount", "absorption" -> instance.absorptionAmount
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Damageable, key: String, value: Any?): OpenResult {
        when (key) {
            "health" -> instance.health = value?.coerceDouble() ?: return OpenResult.successful()
            "absorptionAmount", "absorption-amount", "absorption" -> instance.absorptionAmount = value?.coerceDouble() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}