package com.mcstarrysky.aiyatsbus.module.kether.action.operation

import com.mcstarrysky.aiyatsbus.core.util.VectorUtils
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityShootBowEvent
import taboolib.common.platform.function.submit

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.operation.Aiming
 *
 * @author mical
 * @since 2024/5/19 12:14
 */
object Aiming {

    fun shootBow(range: Double, ticks: Long, event: EntityShootBowEvent) {
        val arrow = event.projectile as AbstractArrow
        val who: Entity = event.entity

        arrow.isGlowing = true
        arrow.shooter = event.entity

        submit(delay = 1L, period = ticks) {
            if (arrow.isDead) {
                cancel()
                return@submit
            }
            if (arrow.isInBlock) {
                cancel()
                return@submit
            }
            for (entity in arrow.getNearbyEntities(range, range, range)) {
                if (entity.uniqueId !== who.uniqueId && entity is LivingEntity
                    && entity !is ArmorStand
                    // && !PermissionUtils.checkIfIsNPC(entity)
                    && entity.hasLineOfSight(who)
                ) {
                    val arrowLoc = arrow.location
                    val destination = entity.location
                    val vector = destination.subtract(arrowLoc).toVector().normalize()
                    val origin = arrow.velocity
                    if (origin.add(vector.multiply(origin.length() / 2)).length() < 5) {
                        VectorUtils.addVelocity(arrow, origin.add(vector.multiply(origin.length() / 2)), false)
                    }
                    break
                }
            }
        }
    }
}