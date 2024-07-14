package com.mcstarrysky.aiyatsbus.core.util

import org.bukkit.entity.LivingEntity
import kotlin.math.PI

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.EntityUtils
 *
 * @author mical
 * @since 2024/7/14 15:30
 */
fun LivingEntity.isBehind(entity: LivingEntity): Boolean {
    if (world.name != entity.world.name) {
        return false
    }
    val directionSelf = entity.location.clone().subtract(location).toVector()
    directionSelf.setY(0)
    val direction = entity.location.direction
    direction.setY(0)
    return directionSelf.angle(direction) < PI / 3
}