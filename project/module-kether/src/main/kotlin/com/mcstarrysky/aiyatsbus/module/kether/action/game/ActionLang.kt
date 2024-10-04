/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.mcstarrysky.aiyatsbus.module.kether.action.game

import com.mcstarrysky.aiyatsbus.core.sendLang
import org.bukkit.entity.Entity
import taboolib.module.kether.KetherParser
import taboolib.module.kether.ParserHolder.option
import taboolib.module.kether.combinationParser

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionLang
 *
 * @author mical
 * @since 2024/7/14 12:41
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