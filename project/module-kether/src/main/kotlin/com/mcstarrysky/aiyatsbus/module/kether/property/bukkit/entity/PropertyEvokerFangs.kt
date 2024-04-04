package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveEntity
import org.bukkit.entity.EvokerFangs
import org.bukkit.entity.LivingEntity
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity.PropertyEvokerFangs
 *
 * @author yanshiqwq
 * @since 2024/4/4 14:14
 */
@AiyatsbusProperty(
    id = "evoker-fangs",
    bind = EvokerFangs::class
)
class PropertyEvokerFangs : AiyatsbusGenericProperty<EvokerFangs>("evoker-fangs") {

    override fun readProperty(instance: EvokerFangs, key: String): OpenResult {
        val property: Any? = when (key) {
            "owner" -> instance.owner
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: EvokerFangs, key: String, value: Any?): OpenResult {
        when (key) {
            "owner" -> instance.owner = value?.liveEntity as LivingEntity
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}