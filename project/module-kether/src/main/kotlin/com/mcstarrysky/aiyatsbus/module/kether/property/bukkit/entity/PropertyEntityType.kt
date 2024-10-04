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