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
package com.mcstarrysky.aiyatsbus.module.kether.operation

import com.mcstarrysky.aiyatsbus.module.kether.operation.operation.Aiming
import com.mcstarrysky.aiyatsbus.module.kether.operation.operation.FastMultiBreak
import com.mcstarrysky.aiyatsbus.module.kether.operation.operation.PickNearItems
import com.mcstarrysky.aiyatsbus.module.kether.operation.operation.Plant
import com.mcstarrysky.aiyatsbus.module.kether.operation.operation.charge.Charge
import org.bukkit.Location
import org.bukkit.entity.*
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.util.Vector
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Function

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.operation.Operations
 *
 * @author mical
 * @since 2024/7/18 18:26
 */
object Operations {

    val registered: ConcurrentHashMap<String, Function<List<Any?>?, Any?>> = ConcurrentHashMap()

    fun register(name: String, func: Function<List<Any?>?, Any?>) {
        registered += name to func
    }

    init {
        register("aiming") { Aiming.shootBow(it) }
        register("charge") { Charge.charge(it) }
        register("plant") { Plant.plant(it) }
        listOf("pickNearItems", "pick-near-items").forEach { name ->
            register(name) { PickNearItems.pickNearItems(it) }
        }
        listOf("fast-multi-break", "fastMultiBreak").forEach { name ->
            register(name) { FastMultiBreak.fastMultiBreak(it) }
        }
        listOf("spawn-arrow", "spawnArrow").forEach { name ->
            register(name) { it ->
                val old = it?.get(3) as? AbstractArrow ?: return@register null // 不是三叉戟啊喂!!!
                val loc = it[0] as Location
                val vec = it[1] as Vector
                val shooter = it[2] as Entity
                val spectral = old is SpectralArrow
                return@register loc.world?.spawnEntity(loc, if (spectral) EntityType.SPECTRAL_ARROW else EntityType.ARROW, CreatureSpawnEvent.SpawnReason.CUSTOM) {
                    if (spectral) {
                        it as SpectralArrow
                        old as SpectralArrow

                        it.velocity = vec
                        it.isGlowing = old.isGlowing
                        it.glowingTicks = old.glowingTicks
                    } else {
                        it as Arrow
                        old as Arrow

                        it.velocity = vec
                        it.shooter = shooter as? LivingEntity
                        it.basePotionType = old.basePotionType
                        old.customEffects.forEach { e -> it.addCustomEffect(e, true) }
                    }
                }
            }
        }
    }
}