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