package com.mcstarrysky.aiyatsbus.module.kether.action

import org.bukkit.entity.Player
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionClassType
 *
 * @author mical
 * @since 2024/4/1 21:27
 */
object ActionClassType {

    private val cache = ConcurrentHashMap<String, Class<*>>()

    /**
     * instance-of &entity is org.bukkit.entity.Player
     */
    @KetherParser(["instance-of"])
    fun instanceOfParser() = combinationParser {
        it.group(any(), command("is", then = text())).apply(it) { obj, cast ->
            now {
                // 缓存
                val clazz = cache.computeIfAbsent(cast) { Class.forName(cast) }
                clazz.isInstance(obj)
            }
        }
    }

    /**
     * cast &entity to org.bukkit.entity.Player
     */
    @KetherParser(["cast"])
    fun castParser() = combinationParser {
        it.group(any(), command("to", then = text())).apply(it) { obj, cast ->
            now {
                // 缓存
                val clazz = cache.computeIfAbsent(cast) { Class.forName(cast) }
                clazz.cast(obj)
            }
        }
    }
}