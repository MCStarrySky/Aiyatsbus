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
import com.mcstarrysky.aiyatsbus.core.util.coerceFloat
import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveEntity
import org.bukkit.entity.FishHook
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity.PropertyFishHook
 *
 * @author yanshiqwq
 * @since 2024/4/4 12:26
 */
@AiyatsbusProperty(
    id = "fish-hook",
    bind = FishHook::class
)
class PropertyFishHook : AiyatsbusGenericProperty<FishHook>("fish-hook") {

    override fun readProperty(instance: FishHook, key: String): OpenResult {
        val property: Any? = when (key) {
            "minWaitTime", "min-wait-time" -> instance.minWaitTime
            "maxWaitTime", "max-wait-time" -> instance.maxWaitTime
            "waitTime", "wait-time" -> instance.waitTime
            "minLureTime", "min-lure-time" -> instance.minLureTime
            "maxLureTime", "max-lure-time" -> instance.maxLureTime
            "minLureAngle", "min-lure-angle" -> instance.minLureAngle
            "maxLureAngle", "max-lure-angle" -> instance.maxLureAngle
            "applyLure", "apply-lure" -> instance.applyLure
            "isInOpenWater", "is-in-open-water", "open-water" -> instance.isInOpenWater
            "hookedEntity", "hooked-entity" -> instance.hookedEntity
            "skyInfluenced", "sky-influenced", "sky" -> instance.isSkyInfluenced
            "rainInfluenced", "rain-influenced", "rain" -> instance.isRainInfluenced
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: FishHook, key: String, value: Any?): OpenResult {
        when (key) {
            "minWaitTime", "min-wait-time" -> instance.minWaitTime = value?.coerceInt() ?: return OpenResult.successful()
            "maxWaitTime", "max-wait-time" -> instance.maxWaitTime = value?.coerceInt() ?: return OpenResult.successful()
            "waitTime", "wait-time" -> instance.waitTime = value?.coerceInt() ?: return OpenResult.successful()

            "minLureTime", "min-lure-time" -> instance.minLureTime = value?.coerceInt() ?: return OpenResult.successful()
            "maxLureTime", "max-lure-time" -> instance.maxLureTime = value?.coerceInt() ?: return OpenResult.successful()
            "lureTime", "lure-time" -> {
                val time = value?.coerceInt() ?: return OpenResult.successful()
                instance.setLureTime(time, time)
            }

            "minLureAngle", "min-lure-angle" -> instance.minLureAngle = value?.coerceFloat() ?: return OpenResult.successful()
            "maxLureAngle", "max-lure-angle" -> instance.maxLureAngle = value?.coerceFloat() ?: return OpenResult.successful()
            "lureAngle", "lure-angle" -> {
                val angle = value?.coerceFloat() ?: return OpenResult.successful()
                instance.setLureAngle(angle, angle)
            }

            "applyLure", "apply-lure" -> instance.applyLure = value?.coerceBoolean() ?: return OpenResult.successful()
            "hookedEntity", "entity" -> instance.hookedEntity = value?.liveEntity ?: return OpenResult.successful()
            "skyInfluenced", "sky-influenced", "sky" -> instance.isSkyInfluenced = value?.coerceBoolean() ?: return OpenResult.successful()
            "rainInfluenced", "rain-influenced", "rain" -> instance.isRainInfluenced = value?.coerceBoolean() ?: return OpenResult.successful()

            "isUnhooked", "unhooked" -> instance.state == FishHook.HookState.UNHOOKED
            "isHookedEntity", "hooked-entity" -> instance.state == FishHook.HookState.HOOKED_ENTITY
            "isBobbing", "bobbing" -> instance.state == FishHook.HookState.BOBBING
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}