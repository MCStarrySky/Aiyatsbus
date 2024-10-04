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

import org.bukkit.metadata.Metadatable
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.platform.util.hasMeta
import taboolib.platform.util.removeMeta
import taboolib.platform.util.setMeta

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionMetadata
 *
 * @author mical
 * @since 2024/4/5 20:54
 */
object ActionMetadata {

    @KetherParser(["setMetadata"])
    fun setMetadataParser() = combinationParser {
        it.group(text(), command("to", then = any()), command("on", then = type<Metadatable>())).apply(it) { key, value, meta ->
            now { meta.setMeta(key, value.toString()) }
        }
    }

    @KetherParser(["removeMetadata"])
    fun removeMetadataParser() = combinationParser {
        it.group(text(), command("on", then = type<Metadatable>())).apply(it) { key, meta ->
            now { meta.removeMeta(key) }
        }
    }

    @KetherParser(["hasMetadata"])
    fun hasMetadataParser() = combinationParser {
        it.group(text(), command("on", then = type<Metadatable>())).apply(it) { key, meta ->
            now { meta.hasMeta(key) }
        }
    }

    @KetherParser(["getMetadata"])
    fun getMetadataParser() = combinationParser {
        it.group(text(), command("on", then = type<Metadatable>()), command("by", then = int())).apply(it) { key, meta, index ->
            now { meta.getMetadata(key)[index].asString() }
        }
    }
}