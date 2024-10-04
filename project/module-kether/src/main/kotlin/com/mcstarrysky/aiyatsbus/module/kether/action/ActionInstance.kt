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