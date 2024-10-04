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

import com.mcstarrysky.aiyatsbus.core.util.mark
import com.mcstarrysky.aiyatsbus.core.util.unmark
import org.bukkit.metadata.Metadatable
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.platform.util.hasMeta

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionMark
 *
 * @author mical
 * @since 2024/3/26 18:12
 */
object ActionMark {

    @KetherParser(["mark"])
    fun markParser() = combinationParser {
        it.group(text(), command("on", then = type<Metadatable>())).apply(it) { key, meta ->
            now { meta.mark(key) }
        }
    }

    @KetherParser(["unmark"])
    fun unmarkParser() = combinationParser {
        it.group(text(), command("on", then = type<Metadatable>())).apply(it) { key, meta ->
            now { meta.unmark(key) }
        }
    }

    @KetherParser(["has-mark", "hasMark"])
    fun hasMark() = combinationParser {
        it.group(text(), command("on", then = type<Metadatable>())).apply(it) { key, meta ->
            now { meta.hasMeta(key) }
        }
    }
}