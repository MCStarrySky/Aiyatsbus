package com.mcstarrysky.aiyatsbus.module.kether.action

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