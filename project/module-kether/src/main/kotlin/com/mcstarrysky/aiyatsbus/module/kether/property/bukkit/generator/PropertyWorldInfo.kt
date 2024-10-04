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
            "environment" -> instance.environment.name
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
