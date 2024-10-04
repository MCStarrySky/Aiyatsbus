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
package com.mcstarrysky.aiyatsbus.module.kether.action.game.item

import com.mcstarrysky.aiyatsbus.module.kether.*
import com.mcstarrysky.aiyatsbus.module.kether.util.getVariable
import com.mcstarrysky.aiyatsbus.module.kether.util.hasNextToken
import com.mcstarrysky.aiyatsbus.module.kether.util.nextPeek
import com.mcstarrysky.aiyatsbus.module.kether.util.setVariable
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor.findInstance
import taboolib.common.platform.Awake
import taboolib.library.kether.QuestAction
import taboolib.library.kether.QuestReader
import taboolib.library.reflex.ReflexClass
import taboolib.module.kether.ScriptActionParser
import taboolib.module.kether.ScriptFrame
import java.util.concurrent.CompletableFuture

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action.item
 *
 * @author Lanscarlos
 * @since 2023-03-23 19:08
 */
class ActionItem : QuestAction<Any?>() {

    val handlers = mutableListOf<Handler<*>>()

    fun resolve(reader: QuestReader): QuestAction<Any?> {
        do {
            val next = reader.nextToken()
            val isRoot = handlers.isEmpty()
            handlers += registry[next.lowercase()]?.resolve(Reader(next, reader, isRoot))
                ?: error("Unknown sub action \"$next\" at item action.")

            // 判断管道是否已关闭
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
            return handlers[0].accept(frame).thenApply { it }
        }

        var previous: CompletableFuture<ItemStack> = (handlers[0] as Transfer).accept(frame)

        for (index in 1 until handlers.size - 1) {
            val current = handlers[index]

            // 除去最后一个 Handler 以及非 Transfer
            if (current !is Transfer) break

            // 判断 future 是否已完成，减少嵌套
            previous = if (previous.isDone) {
                val item = previous.getNow(null)
                frame.setVariable("@Transfer", item, false)
                current.accept(frame)
            } else {
                previous.thenCompose { item ->
                    frame.setVariable("@Transfer", item, false)
                    current.accept(frame)
                }
            }
        }

        // 判断 future 是否已完成，减少嵌套
        return if (previous.isDone) {
            val item = previous.getNow(null)
            frame.setVariable("@Transfer", item, false)
            handlers.last().accept(frame).thenApply { it }
        } else {
            previous.thenCompose { item ->
                frame.setVariable("@Transfer", item, false)
                handlers.last().accept(frame).thenApply { it }
            }
        }
    }

    /*
    * 自动注册包下所有解析器 Resolver
    * */
    @Awake(LifeCycle.LOAD)
    companion object : ClassInjector() {

        private val registry = mutableMapOf<String, Resolver>()

        /**
         * 向 Item 语句注册子语句
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

        @AiyatsbusParser(["item"])
        fun parser() = ScriptActionParser<Any?> {
            ActionItem().resolve(this)
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

        fun transfer(func: Reader.() -> AiyatsbusKether.Parser<ItemStack>): Handler<ItemStack> {
            return Transfer(func(this))
        }

        fun source(): LiveData<ItemStack> {
            return if (isRoot) {
                item()
            } else {
                LiveData {
                    AiyatsbusKether.Action { frame ->
                        CompletableFuture.completedFuture(
                            frame.getVariable<ItemStack>("@Transfer")
                                ?: error("No item source selected. [ERROR: item@$token]")
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
     * 用于传递 Item
     * */
    open class Transfer(parser: AiyatsbusKether.Parser<ItemStack>) : Handler<ItemStack>(parser)
}