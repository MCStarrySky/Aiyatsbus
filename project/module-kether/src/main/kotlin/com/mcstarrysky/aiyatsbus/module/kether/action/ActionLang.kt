package com.mcstarrysky.aiyatsbus.module.kether.action

import com.mcstarrysky.aiyatsbus.core.sendLang
import org.bukkit.entity.Entity
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionLang
 *
 * @author mical
 * @since 2024/3/24 11:49
 */
object ActionLang {

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
}