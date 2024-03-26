package com.mcstarrysky.aiyatsbus.module.kether.action

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