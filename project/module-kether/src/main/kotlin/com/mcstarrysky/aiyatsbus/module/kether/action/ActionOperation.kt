package com.mcstarrysky.aiyatsbus.module.kether.action

import com.mcstarrysky.aiyatsbus.core.util.container.Registry
import com.mcstarrysky.aiyatsbus.module.kether.action.operation.Aiming
import com.mcstarrysky.aiyatsbus.module.kether.action.operation.FastMultiBreak
import com.mcstarrysky.aiyatsbus.module.kether.action.operation.Plant
import org.bukkit.Location
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.SpectralArrow
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.util.Vector
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Function

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionOperation
 *
 * @author mical
 * @since 2024/3/10 15:33
 */
object ActionOperation : Registry<String, Function<List<Any?>?, Any?>>(ConcurrentHashMap()) {

    init {
        register("aiming", Function { Aiming.shootBow(it) })
        register("plant", Function { Plant.plant(it) })
        listOf("fast-multi-break", "fastMultiBreak").forEach { name ->
            register(name, Function { FastMultiBreak.fastMultiBreak(it) })
        }
        listOf("spawn-arrow", "spawnArrow").forEach { name ->
            register(name, Function { it ->
                val old = it?.get(3) as? AbstractArrow ?: return@Function null // 不是三叉戟啊喂!!!
                val loc = it[0] as Location
                val vec = it[1] as Vector
                val shooter = it[2] as Entity
                val spectral = old is SpectralArrow
                return@Function loc.world?.spawnEntity(loc, if (spectral) EntityType.SPECTRAL_ARROW else EntityType.ARROW, CreatureSpawnEvent.SpawnReason.CUSTOM) {
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
            })
        }
    }

    /**
     * operation xxx args [ &int &text ]
     */
    @KetherParser(["operation"])
    fun operationParser() = combinationParser {
        it.group(text(), command("args", then = anyAsList()).option()).apply(it) { operation, args ->
            now {
                ActionOperation[operation]?.apply(args)
            }
        }
    }
}