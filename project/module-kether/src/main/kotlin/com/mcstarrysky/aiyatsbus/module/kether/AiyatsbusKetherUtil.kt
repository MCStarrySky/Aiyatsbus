/*
 * This file is part of Vulpecula, licensed under the MIT License.
 *
 *  Copyright (c) 2018 Bkm016
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
package com.mcstarrysky.aiyatsbus.module.kether

import taboolib.library.kether.LoadError
import taboolib.module.kether.ScriptActionParser
import java.util.concurrent.CompletableFuture

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusKetherUtil
 *
 * @author mical
 * @since 2024/3/10 14:35
 */

internal fun <T> aiyatsbus(func: AiyatsbusReader.() -> AiyatsbusKether.Parser<T>): ScriptActionParser<T> {
    return ScriptActionParser {
        val reader = AiyatsbusReader(this)
        func(reader).also {
            // 重置命名空间
            reader.resetNamespace()
        }.resolve()
    }
}

internal fun aiyatsbusSwitch(func: AiyatsbusReader.() -> Unit): ScriptActionParser<Any?> {
    return ScriptActionParser {
        val dsl = AiyatsbusReader(this).also(func)
        this.mark()
        val next = this.nextToken()
        val method = dsl.methods[next] ?: this.reset().let { dsl.other }
        ?: throw LoadError.NOT_MATCH.create("[${dsl.methods.keys.joinToString(", ")}]", next)

        method().resolve()
    }
}

/**
 * 联合
 * */
fun List<CompletableFuture<*>>.union(): CompletableFuture<List<Any?>> {
    if (this.isEmpty()) {
        // 队列为空
        return CompletableFuture.completedFuture(emptyList())
    } else if (this.size == 1) {
        // 队列仅有一个
        return this[0].thenApply { listOf(it) }
    }

    var previous = this[0]
    for (index in 1 until this.size) {
        val current = this[index]
        previous = if (previous.isDone) {
            current
        } else {
            previous.thenCompose { current }
        }
    }

    return if (previous.isDone) {
        CompletableFuture.completedFuture(
            this.map { it.getNow(null) }
        )
    } else {
        previous.thenApply {
            this.map { it.getNow(null) }
        }
    }
}