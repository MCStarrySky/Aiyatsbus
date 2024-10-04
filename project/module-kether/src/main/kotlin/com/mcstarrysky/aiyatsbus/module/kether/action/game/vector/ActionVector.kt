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
package com.mcstarrysky.aiyatsbus.module.kether.action.game.vector

import com.mcstarrysky.aiyatsbus.module.kether.*
import com.mcstarrysky.aiyatsbus.module.kether.util.getVariable
import com.mcstarrysky.aiyatsbus.module.kether.util.hasNextToken
import com.mcstarrysky.aiyatsbus.module.kether.util.nextPeek
import com.mcstarrysky.aiyatsbus.module.kether.util.setVariable
import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor.findInstance
import taboolib.common.platform.Awake
import taboolib.common.util.Vector
import taboolib.library.kether.QuestAction
import taboolib.library.kether.QuestReader
import taboolib.library.reflex.ReflexClass
import taboolib.module.kether.ScriptActionParser
import taboolib.module.kether.ScriptFrame
import java.util.concurrent.CompletableFuture

/**
 * Vulpecula
 * com.mcstarrysky.aiyatsbus.module.kether.action.vector
 *
 * @author Lanscarlos
 * @since 2023-03-22 14:48
 */
class ActionVector : QuestAction<Any?>() {

    val handlers = mutableListOf<Handler<*>>()

    fun resolve(reader: QuestReader): QuestAction<Any?> {
        do {
            reader.mark()
            val next = reader.nextToken()
            val isRoot = handlers.isEmpty()
            handlers += registry[next.lowercase()]?.resolve(Reader(next, reader, isRoot))
                ?: let {
                    // 默认使用 vector 构建坐标功能
                    reader.reset()
                    ActionVectorBuild.resolve(Reader(next, reader, isRoot))
                }

            // 判断管道是否已经关闭
            if (handlers.lastOrNull() !is Transfer) {
                if (reader.hasNextToken(">>")) {
                    error("Cannot use \">> ${reader.nextPeek()}\", previous action \"$next\" has closed the pipeline.")
                }
                break
            }
        } while (reader.hasNextToken(">>"))
        return this
    }

    override fun process(frame: ScriptFrame): CompletableFuture<Any?> {
        if (handlers.size == 1 || handlers[0] !is Transfer) {
            return handlers[0].accept(frame).thenApply { adaptVector(it) }
        }

        var previous: CompletableFuture<Vector> = (handlers[0] as Transfer).accept(frame)

        for (index in 1 until handlers.size - 1) {
            val current = handlers[index]

            // 除去最后一个 Handler 以及非 Transfer
            if (current !is Transfer) break

            // 判断 future 是否已完成，减少嵌套
            previous = if (previous.isDone) {
                val vector = previous.getNow(null)
                frame.setVariable("@Transfer", vector, false)
                current.accept(frame)
            } else {
                previous.thenCompose { vector ->
                    frame.setVariable("@Transfer", vector, false)
                    current.accept(frame)
                }
            }
        }

        // 判断 future 是否已完成，减少嵌套
        return if (previous.isDone) {
            val vector = previous.getNow(null)
            frame.setVariable("@Transfer", vector, false)
            handlers.last().accept(frame).thenApply { adaptVector(it) }
        } else {
            previous.thenCompose { vector ->
                frame.setVariable("@Transfer", vector, false)
                handlers.last().accept(frame).thenApply { adaptVector(it) }
            }
        }
    }

    fun adaptVector(any: Any?): Any? {
        return if (any is Vector) {
            org.bukkit.util.Vector(any.x, any.y, any.z)
        } else {
            any
        }
    }

    /*
    * 自动注册包下所有解析器 Resolver
    * */
    @Suppress("DuplicatedCode")
    @Awake(LifeCycle.INIT)
    companion object : ClassInjector() {

        private val registry = mutableMapOf<String, Resolver>()

        /**
         * 向 Vector 语句注册子语句
         * @param resolver 子语句解析器
         * */
        fun registerResolver(resolver: Resolver) {
            resolver.name.forEach { registry[it.lowercase()] = resolver }
        }

        override fun visitStart(owner: ReflexClass) {
            val instance = findInstance(owner) ?: return
            if (!Resolver::class.java.isAssignableFrom(instance.javaClass)) return

            val resolver = instance as? Resolver ?: return

            registerResolver(resolver)
        }

        @AiyatsbusParser(["vector", "vec"])
        fun parser() = ScriptActionParser<Any?> {
            ActionVector().resolve(this)
        }
    }

    /**
     * 语句解析器
     * */
    interface Resolver {

        val name: Array<String>

        fun resolve(reader: Reader): Handler<out Any?>
    }

    /**
     * 语句读取器
     * */
    class Reader(val token: String, source: QuestReader, val isRoot: Boolean) : AiyatsbusReader(source) {

        fun <T> handle(func: Reader.() -> AiyatsbusKether.Parser<T>): Handler<T> {
            return Handler(func(this))
        }

        fun transfer(func: Reader.() -> AiyatsbusKether.Parser<Vector>): Handler<Vector> {
            return Transfer(func(this))
        }

        fun source(): LiveData<Vector> {
            return if (isRoot) {
                vector(display = "vector source")
            } else {
                LiveData {
                    AiyatsbusKether.Action { frame ->
                        CompletableFuture.completedFuture(
                            frame.getVariable<Vector>("@Transfer")
                                ?: error("No vector source selected. [ERROR: vector@$token]")
                        )
                    }
                }
            }
        }

    }

    /**
     * 语句处理器
     * */
    open class Handler<T : Any?>(val parser: AiyatsbusKether.Parser<T>) {

        /**
         * 运行
         * */
        open fun accept(frame: ScriptFrame): CompletableFuture<T> {
            return parser.action.run(frame)
        }
    }

    /**
     * 用于传递 Vector
     * */
    open class Transfer(parser: AiyatsbusKether.Parser<Vector>) : Handler<Vector>(parser)
}