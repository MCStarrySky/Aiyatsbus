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

import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity.PropertyMob
 *
 * @author mical
 * @since 2024/4/17 22:19
 */
@AiyatsbusProperty(
    id = "mob",
    bind = Mob::class
)
class PropertyMob : AiyatsbusGenericProperty<Mob>("mob") {

    override fun readProperty(instance: Mob, key: String): OpenResult {
        val property: Any? = when (key) {
            "ambientSound", "ambient-sound", "sound" -> instance.ambientSound?.name
            "target" -> instance.target
            "isAware", "is-aware", "aware" -> instance.isAware
            "isAggressive", "is-aggressive", "aggressive" -> instance.isAggressive
            "isLeftHanded", "is-left-handed", "left-handed" -> instance.isLeftHanded
            "possibleExperienceReward", "possible-experience-reward", "experience-reward" -> instance.possibleExperienceReward
            "headRotationSpeed", "head-rotation-speed" -> instance.headRotationSpeed
            "maxHeadPitch", "max-head-pitch" -> instance.maxHeadPitch
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Mob, key: String, value: Any?): OpenResult {
        when (key) {
            "target" -> instance.target = value as? LivingEntity ?: return OpenResult.successful()
            "isAware", "is-aware", "aware" -> instance.isAware = value?.coerceBoolean() ?: return OpenResult.successful()
            "isAggressive", "is-aggressive", "aggressive" -> instance.isAggressive = value?.coerceBoolean() ?: return OpenResult.successful()
            "isLeftHanded", "is-left-handed", "left-handed" -> instance.isLeftHanded = value?.coerceBoolean() ?: return OpenResult.successful()

            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}