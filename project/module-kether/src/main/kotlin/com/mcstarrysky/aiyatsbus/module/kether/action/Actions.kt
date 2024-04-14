package com.mcstarrysky.aiyatsbus.module.kether.action

import com.mcstarrysky.aiyatsbus.core.sendLang
import com.mcstarrysky.aiyatsbus.core.util.VectorUtils
import com.mcstarrysky.aiyatsbus.core.util.realDamage
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import taboolib.common.platform.function.isPrimaryThread
import taboolib.common.platform.function.submit
import taboolib.library.kether.ArgTypes
import taboolib.module.kether.*
import taboolib.module.nms.getI18nName
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToLong

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.Actions
 *
 * @author mical
 * @since 2024/4/5 00:51
 */
object Actions {

    private val cache = ConcurrentHashMap<String, Class<*>>()

    /**
     * instance-of &entity is org.bukkit.entity.Player
     */
    @KetherParser(["instance-of"], shared = true)
    fun instanceOfParser() = combinationParser {
        it.group(any(), command("is", then = text())).apply(it) { obj, cast ->
            now {
                // 缓存
                val clazz = cache.computeIfAbsent(cast) { Class.forName(cast) }
                clazz.isInstance(obj)
            }
        }
    }

    /**
     * cast &entity to org.bukkit.entity.Player
     */
    @KetherParser(["cast"], shared = true)
    fun castParser() = combinationParser {
        it.group(any(), command("to", then = text())).apply(it) { obj, cast ->
            now {
                // 缓存
                val clazz = cache.computeIfAbsent(cast) { Class.forName(cast) }
                clazz.cast(obj)
            }
        }
    }

    /**
     * 清空列表中的所有元素
     * arr-remove-last &array
     */
    @KetherParser(["arr-clear"], shared = true)
    fun actionArrayRemoveLast() = combinationParser {
        it.group(anyAsList()).apply(it) { array -> now { array.clear() } }
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

    /**
     * send-lang enchant-impact-damaged to &event[entity] with array [ entity-name &event[attacker] ]
     */
    @Suppress("UNCHECKED_CAST")
    @KetherParser(["send-lang"], shared = true)
    fun sendLangParser() = combinationParser {
        it.group(text(), command("to", then = type<Entity>()), command("with", then = anyAsList()).option()).apply(it) { node, to, args ->
            now {
                to.sendLang(node, args = args?.map { it.toString() }?.toTypedArray() ?: emptyArray())
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

    @KetherParser(["a-wait", "a-delay", "a-sleep"])
    fun actionWait() = scriptParser {
        val ticks = it.next(ArgTypes.ACTION)
        actionFuture { f ->
            newFrame(ticks).run<Double>().thenApply { d ->
                val task = submit(delay = (d * 20).roundToLong(), async = !isPrimaryThread) {
                    // 如果玩家在等待过程中离线则终止脚本
                    if (script().sender?.isOnline() == false) {
                        ScriptService.terminateQuest(script())
                        return@submit
                    }
                    f.complete(null)
                }
                addClosable(AutoCloseable { task.cancel() })
            }
        }
    }

    @KetherParser(["strike-lightning", "lightning"], shared = true)
    fun strikeLightningParser() = combinationParser {
        it.group(type<Location>()).apply(it) { loc ->
            now {
                loc.world.strikeLightning(loc)
            }
        }
    }

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

    @KetherParser(["remove-entity"], shared = true)
    fun removeEntityParser() = combinationParser {
        it.group(type<Entity>()).apply(it) { entity -> now { entity.remove() } }
    }

    @KetherParser(["create-explosion"], shared = true)
    fun createExplosionParser() = combinationParser {
        it.group(type<Location>(), float(), command("by", then = type<Entity>()).option(), command("fire", then = bool()).option(), command("break", then = bool()).option()).apply(it) { location, float, entity, fire, break0 ->
            now { location.world.createExplosion(entity, location, float, fire ?: true, break0 ?: true) }
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
                    VectorUtils.addVelocity(target, vector, checkKnockback ?: false)
                } else {
                    target.velocity = vector
                }
            }
        }
    }

    @KetherParser(["drop-item"], shared = true)
    fun actionDropItem() = combinationParser {
        it.group(type<ItemStack>(), command("at", then = type<Location>()), command("naturally", then = bool()).option()).apply(it) { item, loc, naturally ->
            now {
                if (naturally == true) {
                    loc.world.dropItemNaturally(loc, item)
                } else {
                    loc.world.dropItem(loc, item)
                }
            }
        }
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
}