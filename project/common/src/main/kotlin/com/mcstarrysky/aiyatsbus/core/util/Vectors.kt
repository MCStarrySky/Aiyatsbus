package com.mcstarrysky.aiyatsbus.core.util

import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import kotlin.math.max

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.VectorUtils
 *
 * @author mical
 * @since 2024/4/3 00:20
 */
object Vectors {

    private fun isUnsafeVelocity(vector: Vector): Boolean {
        val x = vector.blockX.toDouble()
        val y = vector.blockY.toDouble()
        val z = vector.blockZ.toDouble()
        return x > 4.0 || y > 4.0 || z > 4.0 || x < -4.0 || y < -4.0 || z < -4.0
    }

    private fun getSafeVelocity(x: Double): Double {
        return if (x > 4.0) 4.0 else (max(x, -4.0))
    }

    private fun convertToSafeVelocity(vector: Vector): Vector {
        val x = vector.x
        val y = vector.y
        val z = vector.z
        return Vector(getSafeVelocity(x), getSafeVelocity(y), getSafeVelocity(z))
    }

    fun addVelocity(entity: Entity, vector: Vector, checkKnockback: Boolean) {
        if (checkKnockback && entity is LivingEntity) {
            val instance = entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)
            if (instance != null) {
                val value = instance.value
                if (value >= 1) {
                    return
                }
                if (value > 0) {
                    vector.multiply(1 - value)
                }
            }
        }
        var newVelocity = vector
        if (isUnsafeVelocity(vector)) {
            newVelocity = convertToSafeVelocity(vector)
        }
        try {
            entity.velocity = newVelocity
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }
}