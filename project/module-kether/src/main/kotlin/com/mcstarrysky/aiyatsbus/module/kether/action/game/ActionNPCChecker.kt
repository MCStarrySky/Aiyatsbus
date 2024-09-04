package com.mcstarrysky.aiyatsbus.module.kether.action.game

import com.mcstarrysky.aiyatsbus.core.util.checkIfIsNPC
import org.bukkit.entity.Entity
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.game.ActionNPCChecker
 *
 * @author mical
 * @date 2024/9/4 20:55
 */
object ActionNPCChecker {

    @KetherParser(["isNpc", "isNPC", "is-npc"], shared = true)
    fun isNPCParser() = combinationParser {
        it.group(type<Entity>()).apply(it) { entity -> now { entity.checkIfIsNPC() } }
    }
}