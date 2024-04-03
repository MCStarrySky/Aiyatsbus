package com.mcstarrysky.aiyatsbus.module.kether.action

import com.mcstarrysky.aiyatsbus.core.util.VectorUtils
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionVector
 *
 * @author mical
 * @since 2024/4/3 00:24
 */
object ActionVector {

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
}