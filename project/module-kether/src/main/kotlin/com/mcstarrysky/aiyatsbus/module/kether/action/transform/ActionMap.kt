@file:Suppress("UNCHECKED_CAST")

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
package com.mcstarrysky.aiyatsbus.module.kether.action.transform

import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.transform.ActionMap
 *
 * @author mical
 * @since 2024/7/14 12:55
 */
object ActionMap {

    /**
     * map-size &map
     */
    @KetherParser(["map-size", "map-length"], shared = true)
    fun actionSize() = combinationParser {
        it.group(any()).apply(it) { map ->
            now {
                if (map is Map<*, *>) map.size else map.toString().length
            }
        }
    }

    /**
     * 转换为可变映射
     */
    @KetherParser(["map-mutable"], shared = true)
    fun actionMutable() = combinationParser {
        it.group(any()).apply(it) { map -> now { (map as Map<*, *>).toMutableMap() } }
    }

    /**
     * 打乱映射
     */
    @KetherParser(["map-shuffle"], shared = true)
    fun actionShuffle() = combinationParser {
        it.group(any()).apply(it) { map -> now { (map as Map<*, *>).entries.shuffled().associate { e -> e.key to e.value }.toMutableMap() } }
    }

    /**
     * 反转映射
     */
    @KetherParser(["map-reverse"], shared = true)
    fun actionReverse() = combinationParser {
        it.group(any()).apply(it) { map -> now { (map as Map<*, *>).entries.reversed().associate { e -> e.key to e.value }.toMutableMap() } }
    }

    /**
     * 构建映射
     */
    @KetherParser(["map"], shared = true)
    fun actionMap() = combinationParser {
        it.group(originList()).apply(it) { array -> now { array.chunked(2).associate { e -> e[0] to e[1] }.toMutableMap() } }
    }

    /**
     * 获取映射中的元素
     * map-get &key in &map
     */
    @KetherParser(["map-get"], shared = true)
    fun actionMapGet() = combinationParser {
        it.group(any(), command("in", "of", then = any())).apply(it) { key, map -> now { (map as Map<*, *>)[key] } }
    }

    /**
     * 添加键值对到映射末尾
     * map-put key with value to &map
     */
    @KetherParser(["map-put"], shared = true)
    fun actionMapPut() = combinationParser {
        it.group(any(), command("with", then = any()), command("to", then = any())).apply(it) { key, value, map ->
            now { (map as MutableMap<Any?, Any?>)[key] = value }
        }
    }

    /**
     * 移除映射中的键值对
     * map-remove key in &map
     */
    @KetherParser(["map-remove"], shared = true)
    fun actionMapRemove() = combinationParser {
        it.group(any(), command("in", then = any())).apply(it) { key, map ->
            now { (map as MutableMap<Any?, Any?>) -= key }
        }
    }

    /**
     * 获取映射中的键列表
     * map-keys &map
     */
    @KetherParser(["map-keys"], shared = true)
    fun actionMapKeys() = combinationParser {
        it.group(any()).apply(it) { map -> now { (map as Map<*, *>).keys.toList() } }
    }
}