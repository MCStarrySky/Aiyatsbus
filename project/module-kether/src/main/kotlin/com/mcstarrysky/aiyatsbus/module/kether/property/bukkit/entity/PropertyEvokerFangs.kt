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