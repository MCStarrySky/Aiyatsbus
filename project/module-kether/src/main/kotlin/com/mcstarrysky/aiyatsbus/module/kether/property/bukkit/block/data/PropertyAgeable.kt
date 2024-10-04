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