package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity

import com.mcstarrysky.aiyatsbus.core.util.coerceFloat
import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.GameMode
import org.bukkit.entity.HumanEntity
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity.PropertyHumanEntity
 *
 * @author mical
 * @since 2024/3/25 21:21
 */
@AiyatsbusProperty(
    id = "human-entity",
    bind = HumanEntity::class
)
class PropertyHumanEntity : AiyatsbusGenericProperty<HumanEntity>("human-entity") {

    override fun readProperty(instance: HumanEntity, key: String): OpenResult {
        val property: Any? = when (key) {
            "name" -> instance.name
            "bed-location", "bed-loc", "bed" -> instance.bedLocation
            "uuid" -> instance.uniqueId.toString()

            "game-mode", "gamemode" -> instance.gameMode

            "expToLevel", "exp-to-level" -> instance.expToLevel
            "exhaustion" -> instance.exhaustion
            "foodLevel", "food-level", "food" -> instance.foodLevel
            "attackCooldown", "attack-cooldown" -> instance.attackCooldown
            // TODO
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: HumanEntity, key: String, value: Any?): OpenResult {
        when (key) {
            "game-mode", "gamemode" -> {
                instance.gameMode = value?.toString()?.let { GameMode.valueOf(it) }
                    ?: return OpenResult.successful()
            }
            "exhaustion" -> instance.exhaustion = value?.coerceFloat() ?: return OpenResult.successful()
            "foodLevel", "food-level", "food" -> instance.foodLevel = value?.coerceInt() ?: return OpenResult.successful()
            // TODO
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}