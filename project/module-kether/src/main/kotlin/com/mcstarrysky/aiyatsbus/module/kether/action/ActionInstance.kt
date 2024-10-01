package com.mcstarrysky.aiyatsbus.module.kether.action

import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionInstance
 *
 * @author mical
 * @since 2024/7/14 12:40
 */
object ActionInstance {

    private val cache = ConcurrentHashMap<String, Class<*>>()

    /**
     * instance-of &entity is org.bukkit.entity.Player
     */
    @KetherParser(["instance-of"], shared = true)
    fun instanceOfParser() = combinationParser {
        it.group(any(), command("is", then = text())).apply(it) { obj, cast ->
            now {
                // 尝试避免 ClassNotFoundException
                try {
                    // 缓存
                    val clazz = cache.computeIfAbsent(cast) { Class.forName(cast) }
                    clazz.isInstance(obj)
                } catch (_: Throwable) {
                    false
                }
            }
        }
    }

    /**
     * cast &entity to org.bukkit.entity.Player
     */
    @KetherParser(["cast"], shared = true)
    fun castParser() = combinationParser {
        it.group(any(), command("to", then = text())).apply(it) { obj, cast ->
            now {
                // 尝试避免 ClassNotFoundException
                try {
                    // 缓存
                    val clazz = cache.computeIfAbsent(cast) { Class.forName(cast) }
                    clazz.cast(obj)
                } catch (_: Throwable) {
                    obj
                }
            }
        }
    }
}