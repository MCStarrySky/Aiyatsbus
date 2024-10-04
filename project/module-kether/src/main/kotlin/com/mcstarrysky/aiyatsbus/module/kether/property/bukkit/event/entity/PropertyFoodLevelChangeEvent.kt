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
package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.entity

import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.entity.FoodLevelChangeEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.entity.PropertyFoodLevelChangeEvent
 *
 * @author mical
 * @date 2024/8/19 21:48
 */
@AiyatsbusProperty(
    id = "food-level-change-event",
    bind = FoodLevelChangeEvent::class,
)
class PropertyFoodLevelChangeEvent : AiyatsbusGenericProperty<FoodLevelChangeEvent>("food-level-change") {

    override fun readProperty(instance: FoodLevelChangeEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "entity" -> instance.entity
            "item" -> instance.item
            "foodLevel", "food-level" -> instance.foodLevel
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: FoodLevelChangeEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "foodLevel", "food-level" -> instance.foodLevel = value?.coerceInt() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}