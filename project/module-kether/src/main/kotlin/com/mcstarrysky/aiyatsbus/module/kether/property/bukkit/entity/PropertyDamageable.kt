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