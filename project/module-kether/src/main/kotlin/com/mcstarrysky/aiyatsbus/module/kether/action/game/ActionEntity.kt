package com.mcstarrysky.aiyatsbus.module.kether.action.game

import com.mcstarrysky.aiyatsbus.core.util.Vectors
import com.mcstarrysky.aiyatsbus.core.util.isBehind
import com.mcstarrysky.aiyatsbus.core.util.realDamage
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.module.kether.player
import taboolib.module.nms.getI18nName

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionEntity
 *
 * @author mical
 * @since 2024/7/14 12:41
 */
object ActionEntity {

    @KetherParser(["spawn-entity"], shared = true)
    fun spawnEntityParser() = combinationParser {
        it.group(text(), command("at", then = type<Location>())).apply(it) { type, location ->
            now {
                return@now try {
                    location.world?.spawnEntity(location, EntityType.valueOf(type.uppercase()))
                } catch (_: Throwable) {
                    null
                }
            }
        }
    }

    @KetherParser(["realDamage", "real-damage"], shared = true)
    fun realDamageParser() = combinationParser {
        it.group(type<LivingEntity>(), command("with", then = double()), command("by", then = type<Entity>()).option()).apply(it) { entity, damage, who ->
            now {
                entity.realDamage(damage, who)
            }
        }
    }

    @KetherParser(["entity-name"], shared = true)
    fun entityNameParser() = combinationParser {
        it.group(type<Entity>()).apply(it) { entity ->
            now {
                if (entity is Player) {
                    entity.name
                } else {
                    entity.customName ?: entity.getI18nName(player().castSafely())
                }
            }
        }
    }

    @KetherParser(["remove-entity"], shared = true)
    fun removeEntityParser() = combinationParser {
        it.group(type<Entity>()).apply(it) { entity -> now { entity.remove() } }
    }

    @KetherParser(["add-potion-effect"], shared = true)
    fun actionAddPotionEffect() = combinationParser {
        it.group(
            text(),
            command("on", then = type<LivingEntity>()),
            command("duration", then = int()),
            command("amplifier", then = int()),
            command("ambient", then = bool()).option(),
            command("particles", then = bool()).option(),
            command("icon", then = bool()).option()
        ).apply(it) { type, entity, duration, amplifier, ambient, particles, icon ->
            now {
                entity.addPotionEffect(PotionEffect(PotionEffectType.getByName(type) ?: return@now, duration, amplifier, ambient ?: true, particles ?: true, icon ?: true))
            }
        }
    }

    @KetherParser(["near-by-entities"], shared = true)
    fun actionNearByEntities() = combinationParser {
        it.group(type<Entity>(), command("in", then = double()), double(), double()).apply(it) { entity, r1, r2, r3 ->
            now { entity.getNearbyEntities(r1, r2, r3) }
        }
    }

    @KetherParser(["a-vec-add"])
    fun vecParser() = combinationParser {
        it.group(any(), command("on", then = type<LivingEntity>()), command("safety", then = bool()).option(), command("checkKnockback", then = bool()).option()).apply(it) { vec, target, safety, checkKnockback ->
            now {
                val vector = if (vec is Vector) {
                    vec
                } else if (vec is taboolib.common.util.Vector) {
                    Vector(vec.x, vec.y, vec.z)
                } else {
                    return@now
                }
                if (safety == true) {
                    Vectors.addVelocity(target, vector, checkKnockback ?: false)
                } else {
                    target.velocity = vector
                }
            }
        }
    }

    @KetherParser(["entity-is-behind"], shared = true)
    fun actionEntityIsBehind() = combinationParser {
        it.group(type<LivingEntity>(), type<LivingEntity>()).apply(it) { entity1, entity2 ->
            now { entity1.isBehind(entity2) }
        }
    }
}