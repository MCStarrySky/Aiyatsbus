package com.mcstarrysky.aiyatsbus.module.kether.operation

import com.mcstarrysky.aiyatsbus.core.util.Registry
import com.mcstarrysky.aiyatsbus.module.kether.operation.operation.Aiming
import com.mcstarrysky.aiyatsbus.module.kether.operation.operation.FastMultiBreak
import com.mcstarrysky.aiyatsbus.module.kether.operation.operation.Plant
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
        register("plant") { Plant.plant(it) }
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