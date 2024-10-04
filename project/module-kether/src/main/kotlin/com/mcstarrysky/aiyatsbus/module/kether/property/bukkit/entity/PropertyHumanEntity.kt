/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity

import com.mcstarrysky.aiyatsbus.core.util.coerceFloat
import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveEntity
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveLocation
import org.bukkit.GameMode
import org.bukkit.entity.HumanEntity
import taboolib.common.OpenResult
import taboolib.platform.util.toBukkitLocation

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
            "bedLocation", "bed-location", "bed-loc", "bed" -> instance.bedLocation
            "uuid" -> instance.uniqueId.toString()

            "gameMode", "game-mode", "gamemode" -> instance.gameMode

            "expToLevel", "exp-to-level" -> instance.expToLevel
            "exhaustion" -> instance.exhaustion
            "foodLevel", "food-level", "food" -> instance.foodLevel
            "attackCooldown", "attack-cooldown" -> instance.attackCooldown

            "discoveredRecipes", "recipes-unlocked", "discovered-recipes", "recipes" -> instance.discoveredRecipes
            "enchantmentSeed", "enchantment-seed", "enc-seed", "enchant-seed" -> instance.enchantmentSeed
            "enderChest", "ender-chest", "end-chest" -> instance.enderChest
            "inventory" -> instance.inventory
            "itemOnCursor", "item-on-cursor", "cursor-item" -> instance.itemOnCursor
            "lastDeathLocation", "last-death-location", "las-death", "death-location", "death-loc" -> instance.lastDeathLocation
            "openInventory", "open-inventory", "opening-inventory" -> instance.openInventory
            "sleepTicks", "sleep-ticks" -> instance.sleepTicks

            // TODO: conflict: Same name as method org.bukkit.inventory.EntityEquipment.getItemInMainHand (bukkit.entity.PropertyLivingEntity.mainHand/main-hand)
            "mainHand", "main-hand" -> instance.mainHand

            "starvationRate", "starvation-rate", "starvation" -> instance.starvationRate
            "saturatedRegenRate", "saturated-regen-rate", "saturated-regeneration-rate" -> instance.saturatedRegenRate
            "unsaturatedRegenRate", "unsaturated-regen-rate", "unsaturated-regeneration-rate" -> instance.unsaturatedRegenRate
            "saturation" -> instance.saturation

            "shoulderEntityLeft", "shoulder-entity-left", "left-shoulder" -> instance.shoulderEntityLeft
            "shoulderEntityRight", "shoulder-entity-right", "right-shoulder" -> instance.shoulderEntityRight

            "isBlocking", "blocking" -> instance.isBlocking
            "isHandRaised", "hand-raised", "pre-blocking" -> instance.isHandRaised

            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: HumanEntity, key: String, value: Any?): OpenResult {
        when (key) {
            "gameMode", "game-mode", "gamemode" -> {
                instance.gameMode = value?.toString()?.let { GameMode.valueOf(it) }
                    ?: return OpenResult.successful()
            }
            "exhaustion" -> instance.exhaustion = value?.coerceFloat() ?: return OpenResult.successful()
            "foodLevel", "food-level", "food" -> instance.foodLevel = value?.coerceInt() ?: return OpenResult.successful()
            "enchantmentSeed", "enchantment-seed", "enc-seed", "enchant-seed" -> instance.enchantmentSeed = value?.coerceInt() ?: return OpenResult.successful()
            "lastDeathLocation", "last-death-location", "las-death", "death-location", "death-loc" -> {
                instance.lastDeathLocation = value?.liveLocation?.toBukkitLocation()
                    ?: return OpenResult.successful()
            }
            "saturatedRegenRate", "saturated-regen-rate", "saturated-regeneration-rate" -> instance.saturatedRegenRate = value?.coerceInt() ?: return OpenResult.successful()
            "saturation" -> instance.saturation = value?.coerceFloat() ?: return OpenResult.successful()
            "shoulderEntityLeft", "shoulder-entity-left", "left-shoulder" -> {
                instance.shoulderEntityLeft = value?.liveEntity
                    ?: return OpenResult.successful()
            }
            "shoulderEntityRight", "shoulder-entity-right", "right-shoulder" -> {
                instance.shoulderEntityRight = value?.liveEntity
                    ?: return OpenResult.successful()
            }
            "starvationRate", "starvation-rate", "starvation" -> instance.starvationRate = value?.coerceInt() ?: return OpenResult.successful()
            "unsaturatedRegenRate", "unsaturated-regen-rate", "unsaturated-regeneration-rate" -> instance.unsaturatedRegenRate = value?.coerceInt() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}