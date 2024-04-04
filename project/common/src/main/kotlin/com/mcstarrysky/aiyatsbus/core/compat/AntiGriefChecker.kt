package com.mcstarrysky.aiyatsbus.core.compat

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.compat.AntiGriefChecker
 *
 * @author mical
 * @since 2024/3/21 21:56
 */
object AntiGriefChecker {

    private val checkers = hashSetOf<AntiGrief>()

    fun canBreak(player: Player, location: Location): Boolean {
        return checkers.filter(AntiGrief::checkRunning).all { it.canBreak(player, location) }
    }

    fun canDamage(player: Player, entity: Entity): Boolean {
        return checkers.filter(AntiGrief::checkRunning).all { it.canDamage(player, entity) }
    }

    fun registerNewCompatibility(comp: AntiGrief) {
        checkers += comp
    }
}