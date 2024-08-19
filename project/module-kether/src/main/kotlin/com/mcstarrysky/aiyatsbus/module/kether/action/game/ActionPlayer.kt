package com.mcstarrysky.aiyatsbus.module.kether.action.game

import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import org.bukkit.entity.Player
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.game.ActionPlayer
 *
 * @author mical
 * @since 2024/8/19 10:47
 */
object ActionPlayer {

    @KetherParser(["give-exp"], shared = true)
    fun giveExpParser() = combinationParser {
        it.group(int(), command("to", then = type<Player>()), command("mending", then = bool()).option()).apply(it) { exp, player, mending ->
            now { player.giveExp(exp, mending.coerceBoolean(true)) } // 第二个参数 Boolean 为是否应用经验修补
        }
    }
}