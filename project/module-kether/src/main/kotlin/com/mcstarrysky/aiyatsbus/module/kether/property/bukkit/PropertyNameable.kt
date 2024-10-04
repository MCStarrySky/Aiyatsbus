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
package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.Nameable
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.PropertyNameable
 *
 * @author yanshiqwq
 * @since 2024/4/1 01:21
 */
@AiyatsbusProperty(
    id = "nameable",
    bind = Nameable::class
)
class PropertyNameable : AiyatsbusGenericProperty<Nameable>("nameable") {

    override fun readProperty(instance: Nameable, key: String): OpenResult {
        val property: Any? = when (key) {
            "customName", "custom-name" -> instance.customName
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }
    override fun writeProperty(instance: Nameable, key: String, value: Any?): OpenResult {
        when (key) {
            "customName", "custom-name" -> instance.customName = value.toString()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}