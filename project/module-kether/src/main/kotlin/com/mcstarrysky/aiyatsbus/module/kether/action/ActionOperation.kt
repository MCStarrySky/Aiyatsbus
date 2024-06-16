package com.mcstarrysky.aiyatsbus.module.kether.action

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusParser
import com.mcstarrysky.aiyatsbus.module.kether.action.operation.Aiming
import com.mcstarrysky.aiyatsbus.module.kether.action.operation.FastMultiBreak
import com.mcstarrysky.aiyatsbus.module.kether.action.operation.Plant
import com.mcstarrysky.aiyatsbus.module.kether.aiyatsbus
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.SpectralArrow
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.util.Vector
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.kether.player

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionOperation
 *
 * @author mical
 * @since 2024/3/10 15:33
 */
@Deprecated("等待重构以支持添加自定义操作")
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
                combine(trim("at", then = any()), trim("by-vec", then = any()), trim("by-shooter", then = entity()), optional("by-old", then = entity())) { loc, vec, shooter, old ->
                    if (old !is AbstractArrow) return@combine null // 不是三叉戟啊喂!!!
                    vec as Vector
                    loc as Location
                    val spectral = old is SpectralArrow
                    loc.world?.spawnEntity(loc, if (spectral) EntityType.SPECTRAL_ARROW else EntityType.ARROW, CreatureSpawnEvent.SpawnReason.CUSTOM) {
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
            "aiming" -> {
                combine(any(), trim("by", then = double()), trim("period", then = long()), trim("color", then = text())) { event, range, ticks, color ->
                    event as EntityShootBowEvent
                    Aiming.shootBow(range, ticks, event, ChatColor.values().firstOrNull { it.name.lowercase() == color.lowercase() } ?: ChatColor.WHITE)
                }
            }
            else -> error("unknown operation")
        }
    }
}