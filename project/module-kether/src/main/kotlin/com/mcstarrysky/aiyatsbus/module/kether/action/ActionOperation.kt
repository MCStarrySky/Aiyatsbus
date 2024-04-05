package com.mcstarrysky.aiyatsbus.module.kether.action

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusParser
import com.mcstarrysky.aiyatsbus.module.kether.action.operation.FastMultiBreak
import com.mcstarrysky.aiyatsbus.module.kether.action.operation.Plant
import com.mcstarrysky.aiyatsbus.module.kether.aiyatsbus
import org.bukkit.Location
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.util.Vector
import taboolib.module.kether.player

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionOperation
 *
 * @author mical
 * @since 2024/3/10 15:33
 */
object ActionOperation {

    @Suppress("UNCHECKED_CAST")
    @AiyatsbusParser(["operation"])
    fun operation() = aiyatsbus {
        when (nextToken()) {
            "fast-multi-break", "fastMultiBreak" -> {
                combine(any(), int()) { breaks, speed ->
                    FastMultiBreak.fastMultiBreak(
                        player().castSafely()!!,
                        (breaks as List<Location>).toMutableList(),
                        speed
                    )
                }
            }
            "plant" -> {
                combine(int(), text()) { side, seeds ->
                    Plant.plant(player().castSafely()!!, side, seeds)
                }
            }
            "spawn-arrow", "spawnArrow" -> {
                combine(trim("at", then = any()), trim("by-vec", then = any()), trim("by-shooter", then = entity())) { loc, vec, shooter ->
                    vec as Vector
                    loc as Location
                    loc.world?.spawnEntity(loc, EntityType.ARROW, CreatureSpawnEvent.SpawnReason.CUSTOM) {
                        it.velocity = vec
                        (it as AbstractArrow).shooter = shooter as? LivingEntity
                    }
                }
            }
            else -> error("unknown operation")
        }
    }
}