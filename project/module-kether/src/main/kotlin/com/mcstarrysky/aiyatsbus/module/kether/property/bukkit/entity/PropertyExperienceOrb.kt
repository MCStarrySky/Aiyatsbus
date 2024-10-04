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

import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.entity.ExperienceOrb
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity.PropertyExperienceOrb
 *
 * @author mical
 * @since 2024/8/19 08:45
 */
@AiyatsbusProperty(
    id = "experience-orb",
    bind = ExperienceOrb::class
)
class PropertyExperienceOrb : AiyatsbusGenericProperty<ExperienceOrb>("experience-orb") {

    override fun readProperty(instance: ExperienceOrb, key: String): OpenResult {
        val property: Any? = when (key) {
            "experience" -> instance.experience
            "count" -> instance.count
            "isFromBottle", "is-from-bottle" -> instance.isFromBottle
            "triggerEntityId", "trigger-entity-id" -> instance.triggerEntityId.toString()
            "sourceEntityId", "source-entity-id" -> instance.sourceEntityId.toString()
            "spawnReason", "spawn-reason" -> instance.spawnReason.name
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: ExperienceOrb, key: String, value: Any?): OpenResult {
        when (key) {
            "experience" -> instance.experience = value?.coerceInt(0) ?: return OpenResult.successful()
            "count" -> instance.count = value?.coerceInt(0) ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}