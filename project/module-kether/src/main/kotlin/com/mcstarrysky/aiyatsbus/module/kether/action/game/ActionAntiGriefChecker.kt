package com.mcstarrysky.aiyatsbus.module.kether.action.game

import com.mcstarrysky.aiyatsbus.core.compat.AntiGriefChecker
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusParser
import com.mcstarrysky.aiyatsbus.module.kether.aiyatsbus
import taboolib.platform.util.toBukkitLocation

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionAntiGriefChecker
 *
 * @author mical
 * @since 2024/3/23 18:08
 */
object ActionAntiGriefChecker {

    @AiyatsbusParser(["anti-grief-checker", "anti-grief", "agc"])
    fun antiGriefChecker() = aiyatsbus {
        when (nextToken()) {
            "can-break" -> {
                combine(player(), location()) { p, l ->
                    AntiGriefChecker.canBreak(p, l.toBukkitLocation())
                }
            }
            "can-damage" -> {
                combine(player(), entity()) { p, e ->
                    AntiGriefChecker.canDamage(p, e)
                }
            }
            else -> error("unknown operation")
        }
    }
}