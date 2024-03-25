package com.mcstarrysky.aiyatsbus.module.kether.action

import org.bukkit.Location
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionStrikeLightning
 *
 * @author mical
 * @since 2024/3/25 21:11
 */
object ActionStrikeLightning {

    @KetherParser(["strike-lightning", "lightning"])
    fun strikeLightningParser() = combinationParser {
        it.group(type<Location>()).apply(it) { loc ->
            now {
                loc.world.strikeLightning(loc)
            }
        }
    }
}