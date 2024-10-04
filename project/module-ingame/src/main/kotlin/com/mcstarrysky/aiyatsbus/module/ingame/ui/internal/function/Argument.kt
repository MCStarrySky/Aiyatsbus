@file:Suppress("unused")

/*
 * This file is part of ParrotX, licensed under the MIT License.
 *
 *  Copyright (c) 2020 Legoshi
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function

inline fun <reified T> Map<*, *>.valueOrNull(node: String): T? {
    val value = this[node] ?: return null
    return requireNotNull(value as? T) {
        "配置项 $node 类型不正确 (要求: ${T::class.java.simpleName}, 实际: ${value::class.java.simpleName})"
    }
}

inline fun <reified T> Map<*, *>.value(node: String): T = requireNotNull(valueOrNull(node)) { "缺少配置项 $node" }

inline fun <reified T> Array<*>.argumentOrNull(index: Int): T? {
    val value = elementAtOrNull(index) ?: return null
    return requireNotNull(value as? T) {
        "参数 $index 类型不正确 (要求: ${T::class.java.simpleName}, 实际: ${value::class.java.simpleName})"
    }
}

inline fun <reified T> Array<*>.argument(index: Int): T = requireNotNull(argumentOrNull(index)) { "缺少参数 $index" }

fun <T> Iterator<T>.nextOrNull(): T? {
    return if (hasNext()) next() else null
}