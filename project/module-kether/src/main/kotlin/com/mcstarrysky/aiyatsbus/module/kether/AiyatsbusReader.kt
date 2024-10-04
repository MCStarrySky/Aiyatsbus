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

import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.frameBy
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveBoolean
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveColor
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveDouble
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveEntity
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveFloat
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveInt
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveInventory
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveItemStack
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveLocation
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveLong
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.livePlayer
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveShort
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveStringList
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveVector
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.readerOf
import com.mcstarrysky.aiyatsbus.module.kether.action.ActionBlock
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.common.util.Location
import taboolib.common.util.Vector
import taboolib.library.kether.ArgTypes
import taboolib.library.kether.ParsedAction
import taboolib.library.kether.QuestReader
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.kether.ScriptFrame
import taboolib.module.kether.run
import java.awt.Color
import java.util.ArrayList
import java.util.concurrent.CompletableFuture

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.AiyatsbusKether
 *
 * @author Lanscarlos
 * @since 2023-02-26 16:39
 */
open class AiyatsbusReader(private val source: QuestReader) {

    val argumentPrefixPattern = "-\\D+".toRegex()
    val namespace = mutableListOf<String>()
    val methods = HashMap<String, () -> AiyatsbusKether.Parser<Any?>>()
    var other: (() -> AiyatsbusKether.Parser<out Any?>)? = null
        private set

    fun case(vararg name: String, func: () -> AiyatsbusKether.Parser<Any?>) {
        name.forEach { methods[it] = func }
    }

    fun other(func: () -> AiyatsbusKether.Parser<out Any?>) {
        other = func
    }

    /*
    * QuestReader 操作
    * */

    fun namespace(): ArrayList<String> {
        return source.getProperty<ArrayList<String>>("namespace")!!
    }

    fun addNamespace(namespace: String) {
        this.namespace += namespace
        namespace() += namespace
    }

    fun resetNamespace() {
        if (this.namespace.isEmpty()) return

        val namespace = namespace()
        for (it in this.namespace) {
            namespace -= it
        }
    }

    fun mark(): Int {
        source.mark()
        return source.mark
    }

    fun reset() {
        source.reset()
    }

    fun nextToken(): String {
        return source.nextToken()
    }

    fun peekToken(): String {
        source.mark()
        return source.nextToken().also { source.reset() }
    }

    fun expectToken(vararg expect: String): Boolean {
        if (expect.isEmpty()) return false
        source.mark()
        return if (source.nextToken() in expect) {
            true
        } else {
            source.reset()
            false
        }
    }

    fun readAction(): ParsedAction<*> {
        return if (this.expectToken("{")) {
            val actions = mutableListOf<ParsedAction<*>>()
            while (!expectToken("}")) {
                actions += source.nextParsedAction()
            }
            ParsedAction(ActionBlock(actions))
        } else {
            source.nextParsedAction()
        }
    }

    fun readActionList(): List<ParsedAction<*>> {
        return source.next(ArgTypes.listOf(ArgTypes.ACTION))
    }

    /*
    * Action<T> 函数
    * */

    fun <R> now(func: ScriptFrame.() -> R): AiyatsbusKether.Action<R> {
        return AiyatsbusKether.Action { CompletableFuture.completedFuture(func(it)) }
    }

    fun <R> future(func: ScriptFrame.() -> CompletableFuture<R>): AiyatsbusKether.Action<R> {
        return AiyatsbusKether.Action(func)
    }

    /*
    * LiveData 操作
    * */

    fun <T> trim(vararg expect: String, then: LiveData<T>): LiveData<T> {
        return then.trim(*expect)
    }

    fun <T> expect(vararg expect: String, then: LiveData<T>): LiveData<T> {
        return then.expect(*expect)
    }

    fun <T> optional(vararg expect: String, then: LiveData<T>): LiveData<T?> {
        return then.optional(*expect)
    }

    fun <T> optional(vararg expect: String, then: LiveData<T>, def: T): LiveData<T> {
        return then.optional(*expect, def = def)
    }

    /**
     * 额外参数 <br>
     * 适用于 -prefix {xxx} 的情况
     * @param prefix 参数识别前缀
     * @param then 参数处理
     * */
    fun <T> argument(vararg prefix: String, then: LiveData<T>): LiveData<T?> {
        return LiveDataProxy(*prefix, source = then.optional(), def = null)
    }

    /**
     * 额外参数 <br>
     * 适用于 -prefix {xxx} 的情况
     * @param prefix 参数识别前缀
     * @param then 参数处理
     * @param def 默认值
     * */
    fun <T> argument(vararg prefix: String, then: LiveData<T>, def: T): LiveData<T> {
        return LiveDataProxy(*prefix, source = then, def = def)
    }

    fun action(): LiveData<ParsedAction<*>> {
        return readerOf { it.readAction() }
    }

    fun any(): LiveData<Any?> = frameBy { it }

    fun list(): LiveData<List<*>> = LiveData {
        val list = this.readActionList()
        AiyatsbusKether.Action { frame ->
            list.map { frame.run(it) }.union()
        }
    }

    fun boolOrNull(): LiveData<Boolean?> = frameBy { it?.liveBoolean }

    fun bool(def: Boolean? = null, display: String = "boolean"): LiveData<Boolean> {
        return frameBy { it?.liveBoolean ?: def ?: error("No $display selected.") }
    }

    fun shortOrNull(): LiveData<Short?> = frameBy { it?.liveShort }

    fun short(def: Short? = null, display: String = "short"): LiveData<Short> {
        return frameBy { it?.liveShort ?: def ?: error("No $display selected.") }
    }

    fun intOrNull(): LiveData<Int?> = frameBy { it?.liveInt }

    fun int(def: Int? = null, display: String = "int"): LiveData<Int> {
        return frameBy { it?.liveInt ?: def ?: error("No $display selected.") }
    }

    fun longOrNull(): LiveData<Long?> = frameBy { it?.liveLong }

    fun long(def: Long? = null, display: String = "long"): LiveData<Long> {
        return frameBy { it?.liveLong ?: def ?: error("No $display selected.") }
    }

    fun floatOrNull(): LiveData<Float?> = frameBy { it?.liveFloat }

    fun float(def: Float? = null, display: String = "float"): LiveData<Float> {
        return frameBy { it?.liveFloat ?: def ?: error("No $display selected.") }
    }

    fun doubleOrNull(): LiveData<Double?> = frameBy { it?.liveDouble }

    fun double(def: Double? = null, display: String = "double"): LiveData<Double> {
        return frameBy { it?.liveDouble ?: def ?: error("No $display selected.") }
    }

    fun literal(): LiveData<String> = readerOf { it.nextToken() }

    fun textOrNull(): LiveData<String?> = frameBy { it?.toString() }
    fun text(def: String? = null, display: String = "text"): LiveData<String> {
        return frameBy { it?.toString() ?: def ?: error("No $display selected.") }
    }

    fun multilineOrNull(): LiveData<List<String>?> = frameBy { it?.liveStringList }
    fun multiline(def: List<String>? = null, display: String = "multiline text"): LiveData<List<String>> {
        return frameBy { it?.liveStringList ?: def ?: error("No $display selected.") }
    }

    fun stringOrList(): LiveData<Any> = frameBy {
        when (it) {
            is String -> it
            is Array<*> -> {
                it.mapNotNull { el -> el?.toString() }
            }
            is Collection<*> -> {
                it.mapNotNull { el -> el?.toString() }
            }
            else -> error("No text or list selected.")
        }
    }

    fun vectorOrNull(): LiveData<Vector?> = frameBy { it?.liveVector }
    fun vector(def: Vector? = null, display: String = "vector"): LiveData<Vector> {
        return frameBy { it?.liveVector ?: def ?: error("No $display selected.") }
    }

    fun locationOrNull(): LiveData<Location?> = frameBy { it?.liveLocation }
    fun location(def: Location? = null, display: String = "location"): LiveData<Location> {
        return frameBy { it?.liveLocation ?: def ?: error("No $display selected.") }
    }

    fun colorOrNull(): LiveData<Color?> = frameBy { it?.liveColor }
    fun color(def: Color? = null, display: String = "color"): LiveData<Color> {
        return frameBy { it?.liveColor ?: def ?: error("No $display selected.") }
    }

    fun entityOrNull(): LiveData<Entity?> = frameBy { it?.liveEntity }
    fun entity(def: Entity? = null, display: String = "entity"): LiveData<Entity> {
        return frameBy { it?.liveEntity ?: def ?: error("No $display selected.") }
    }

    fun playerOrNull(): LiveData<Player?> = frameBy { it?.livePlayer }
    fun player(def: Player? = null, display: String = "player"): LiveData<Player> {
        return frameBy { it?.livePlayer ?: def ?: error("No $display selected.") }
    }

    fun itemOrNull(): LiveData<ItemStack?> = frameBy { it?.liveItemStack }
    fun item(def: ItemStack? = null, display: String = "itemStack"): LiveData<ItemStack> {
        return frameBy { it?.liveItemStack ?: def ?: error("No $display selected.") }
    }

    fun inventoryOrNull(): LiveData<Inventory?> = frameBy { it?.liveInventory }
    fun inventory(def: Inventory? = null, display: String = "inventory"): LiveData<Inventory> {
        return frameBy { it?.liveInventory ?: def ?: error("No $display selected.") }
    }

    fun applyLiveData(vararg liveData: LiveData<*>) {
        if (liveData.isEmpty()) return

        val proxy = mutableListOf<LiveDataProxy<*>>()
        var breakpoint = 0

        for ((index, it) in liveData.withIndex()) {
            if (it is LiveDataProxy<*>) {
                proxy.add(it)
            } else if (proxy.isEmpty()) {
                // 未检索到附加参数，读取语句
                it.accept(reader = this)
            } else {
                // 已检索到附加参数，将此处设置为断点
                breakpoint = index
                break
            }
        }

        if (proxy.isNotEmpty()) {
            // 读取附加参数
            while (peekToken().matches(argumentPrefixPattern)) {
                val prefix = nextToken().substring(1)
                for (it in proxy) {
                    it.accept(prefix, reader = this)
                }
            }

            // 读取剩余语句
            if (breakpoint > 0) {
                // 已定位断点，跳过断点前的语句；（断点必须大于零，因为前面必须至少有一个附加参数）
                for (index in breakpoint until liveData.size) {
                    liveData[index].accept(reader = this)
                }
            }
        }

        // 没有附加参数时，所有语句都已被读取，此时不应该有剩余语句
    }

    /*
     * discrete(...)
     * 无参数
     * */

    inline fun <R> discrete(crossinline func: ScriptFrame.() -> R): AiyatsbusKether.Parser<R> {
        return AiyatsbusKether.Parser { CompletableFuture.completedFuture(func(it)) }
    }

    inline fun <R> discreteOf(crossinline func: () -> AiyatsbusKether.Action<R>): AiyatsbusKether.Parser<R> {
        return AiyatsbusKether.Parser { frame ->
            func().run(frame)
        }
    }

    /*
    * combine(...)
    * func 返回 R
    * */

    inline fun <P1, R> combine(
        p1: LiveData<P1>,
        crossinline func: ScriptFrame.(P1) -> R
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1)
        return AiyatsbusKether.Parser { frame ->
            p1.accept(frame).thenApply { t1 ->
                func(frame, t1)
            }
        }
    }

    inline fun <P1, R> combineOf(
        p1: LiveData<P1>,
        crossinline func: ScriptFrame.(P1) -> CompletableFuture<R>
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1)
        return AiyatsbusKether.Parser { frame ->
            p1.accept(frame).thenCompose { t1 -> func(frame, t1) }
        }
    }

    inline fun <P1, P2, R> combine(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        crossinline func: ScriptFrame.(P1, P2) -> R
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame)
            ).thenApply {
                func(frame, it.t1, it.t2)
            }
        }
    }

    inline fun <P1, P2, R> combineOf(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        crossinline func: ScriptFrame.(P1, P2) -> CompletableFuture<R>
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame)
            ).thenCompose {
                func(frame, it.t1, it.t2)
            }
        }
    }

    fun <P1, P2, P3, R> combine(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        func: ScriptFrame.(P1, P2, P3) -> R
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame)
            ).thenApply {
                func(frame, it.t1, it.t2, it.t3)
            }
        }
    }

    fun <P1, P2, P3, R> combineOf(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        func: ScriptFrame.(P1, P2, P3) -> CompletableFuture<R>
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame)
            ).thenCompose {
                func(frame, it.t1, it.t2, it.t3)
            }
        }
    }

    fun <P1, P2, P3, P4, R> combine(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        func: ScriptFrame.(P1, P2, P3, P4) -> R
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame)
            ).thenApply {
                func(frame, it.t1, it.t2, it.t3, it.t4)
            }
        }
    }

    fun <P1, P2, P3, P4, R> combineOf(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        func: ScriptFrame.(P1, P2, P3, P4) -> CompletableFuture<R>
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame)
            ).thenCompose {
                func(frame, it.t1, it.t2, it.t3, it.t4)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, R> combine(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        func: ScriptFrame.(P1, P2, P3, P4, P5) -> R
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame)
            ).thenApply {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, R> combineOf(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        func: ScriptFrame.(P1, P2, P3, P4, P5) -> CompletableFuture<R>
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
            ).thenCompose {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, R> combine(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6) -> R
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame)
            ).thenApply {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5, it.t6)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, R> combineOf(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6) -> CompletableFuture<R>
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame)
            ).thenCompose {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5, it.t6)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, R> combine(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7) -> R
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame)
            ).thenApply {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5, it.t6, it.t7)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, R> combineOf(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7) -> CompletableFuture<R>
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame)
            ).thenCompose {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5, it.t6, it.t7)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, R> combine(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8) -> R
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame)
            ).thenApply {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5, it.t6, it.t7, it.t8)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, R> combineOf(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8) -> CompletableFuture<R>
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame)
            ).thenCompose {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5, it.t6, it.t7, it.t8)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, R> combine(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        p9: LiveData<P9>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8, P9) -> R
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8, p9)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame),
                p9.accept(frame)
            ).thenApply {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5, it.t6, it.t7, it.t8, it.t9)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, R> combineOf(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        p9: LiveData<P9>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8, P9) -> CompletableFuture<R>
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8, p9)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame),
                p9.accept(frame)
            ).thenCompose {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5, it.t6, it.t7, it.t8, it.t9)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, R> combine(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        p9: LiveData<P9>,
        p10: LiveData<P10>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10) -> R
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame),
                p9.accept(frame),
                p10.accept(frame)
            ).thenApply {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5, it.t6, it.t7, it.t8, it.t9, it.t10)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, R> combineOf(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        p9: LiveData<P9>,
        p10: LiveData<P10>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10) -> CompletableFuture<R>
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame),
                p9.accept(frame),
                p10.accept(frame)
            ).thenCompose {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5, it.t6, it.t7, it.t8, it.t9, it.t10)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, R> combine(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        p9: LiveData<P9>,
        p10: LiveData<P10>,
        p11: LiveData<P11>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11) -> R
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame),
                p9.accept(frame),
                p10.accept(frame),
                p11.accept(frame)
            ).thenApply {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5, it.t6, it.t7, it.t8, it.t9, it.t10, it.t11)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, R> combineOf(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        p9: LiveData<P9>,
        p10: LiveData<P10>,
        p11: LiveData<P11>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11) -> CompletableFuture<R>
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame),
                p9.accept(frame),
                p10.accept(frame),
                p11.accept(frame)
            ).thenCompose {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5, it.t6, it.t7, it.t8, it.t9, it.t10, it.t11)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, R> combine(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        p9: LiveData<P9>,
        p10: LiveData<P10>,
        p11: LiveData<P11>,
        p12: LiveData<P12>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12) -> R
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame),
                p9.accept(frame),
                p10.accept(frame),
                p11.accept(frame),
                p12.accept(frame)
            ).thenApply {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5, it.t6, it.t7, it.t8, it.t9, it.t10, it.t11, it.t12)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, R> combineOf(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        p9: LiveData<P9>,
        p10: LiveData<P10>,
        p11: LiveData<P11>,
        p12: LiveData<P12>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12) -> CompletableFuture<R>
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame),
                p9.accept(frame),
                p10.accept(frame),
                p11.accept(frame),
                p12.accept(frame)
            ).thenCompose {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5, it.t6, it.t7, it.t8, it.t9, it.t10, it.t11, it.t12)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, R> combine(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        p9: LiveData<P9>,
        p10: LiveData<P10>,
        p11: LiveData<P11>,
        p12: LiveData<P12>,
        p13: LiveData<P13>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13) -> R
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame),
                p9.accept(frame),
                p10.accept(frame),
                p11.accept(frame),
                p12.accept(frame),
                p13.accept(frame)
            ).thenApply {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5, it.t6, it.t7, it.t8, it.t9, it.t10, it.t11, it.t12, it.t13)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, R> combineOf(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        p9: LiveData<P9>,
        p10: LiveData<P10>,
        p11: LiveData<P11>,
        p12: LiveData<P12>,
        p13: LiveData<P13>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13) -> CompletableFuture<R>
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame),
                p9.accept(frame),
                p10.accept(frame),
                p11.accept(frame),
                p12.accept(frame),
                p13.accept(frame)
            ).thenCompose {
                func(frame, it.t1, it.t2, it.t3, it.t4, it.t5, it.t6, it.t7, it.t8, it.t9, it.t10, it.t11, it.t12, it.t13)
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, R> combine(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        p9: LiveData<P9>,
        p10: LiveData<P10>,
        p11: LiveData<P11>,
        p12: LiveData<P12>,
        p13: LiveData<P13>,
        p14: LiveData<P14>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14) -> R
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame),
                p9.accept(frame),
                p10.accept(frame),
                p11.accept(frame),
                p12.accept(frame),
                p13.accept(frame),
                p14.accept(frame)
            ).thenApply {
                func(
                    frame,
                    it.t1,
                    it.t2,
                    it.t3,
                    it.t4,
                    it.t5,
                    it.t6,
                    it.t7,
                    it.t8,
                    it.t9,
                    it.t10,
                    it.t11,
                    it.t12,
                    it.t13,
                    it.t14
                )
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, R> combineOf(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        p9: LiveData<P9>,
        p10: LiveData<P10>,
        p11: LiveData<P11>,
        p12: LiveData<P12>,
        p13: LiveData<P13>,
        p14: LiveData<P14>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14) -> CompletableFuture<R>
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame),
                p9.accept(frame),
                p10.accept(frame),
                p11.accept(frame),
                p12.accept(frame),
                p13.accept(frame),
                p14.accept(frame)
            ).thenCompose {
                func(
                    frame,
                    it.t1,
                    it.t2,
                    it.t3,
                    it.t4,
                    it.t5,
                    it.t6,
                    it.t7,
                    it.t8,
                    it.t9,
                    it.t10,
                    it.t11,
                    it.t12,
                    it.t13,
                    it.t14
                )
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, R> combine(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        p9: LiveData<P9>,
        p10: LiveData<P10>,
        p11: LiveData<P11>,
        p12: LiveData<P12>,
        p13: LiveData<P13>,
        p14: LiveData<P14>,
        p15: LiveData<P15>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15) -> R
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame),
                p9.accept(frame),
                p10.accept(frame),
                p11.accept(frame),
                p12.accept(frame),
                p13.accept(frame),
                p14.accept(frame),
                p15.accept(frame)
            ).thenApply {
                func(
                    frame,
                    it.t1,
                    it.t2,
                    it.t3,
                    it.t4,
                    it.t5,
                    it.t6,
                    it.t7,
                    it.t8,
                    it.t9,
                    it.t10,
                    it.t11,
                    it.t12,
                    it.t13,
                    it.t14,
                    it.t15
                )
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, R> combineOf(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        p9: LiveData<P9>,
        p10: LiveData<P10>,
        p11: LiveData<P11>,
        p12: LiveData<P12>,
        p13: LiveData<P13>,
        p14: LiveData<P14>,
        p15: LiveData<P15>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15) -> CompletableFuture<R>
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame),
                p9.accept(frame),
                p10.accept(frame),
                p11.accept(frame),
                p12.accept(frame),
                p13.accept(frame),
                p14.accept(frame),
                p15.accept(frame)
            ).thenCompose {
                func(
                    frame,
                    it.t1,
                    it.t2,
                    it.t3,
                    it.t4,
                    it.t5,
                    it.t6,
                    it.t7,
                    it.t8,
                    it.t9,
                    it.t10,
                    it.t11,
                    it.t12,
                    it.t13,
                    it.t14,
                    it.t15
                )
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, R> combine(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        p9: LiveData<P9>,
        p10: LiveData<P10>,
        p11: LiveData<P11>,
        p12: LiveData<P12>,
        p13: LiveData<P13>,
        p14: LiveData<P14>,
        p15: LiveData<P15>,
        p16: LiveData<P16>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16) -> R
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame),
                p9.accept(frame),
                p10.accept(frame),
                p11.accept(frame),
                p12.accept(frame),
                p13.accept(frame),
                p14.accept(frame),
                p15.accept(frame),
                p16.accept(frame)
            ).thenApply {
                func(
                    frame,
                    it.t1,
                    it.t2,
                    it.t3,
                    it.t4,
                    it.t5,
                    it.t6,
                    it.t7,
                    it.t8,
                    it.t9,
                    it.t10,
                    it.t11,
                    it.t12,
                    it.t13,
                    it.t14,
                    it.t15,
                    it.t16
                )
            }
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, R> combineOf(
        p1: LiveData<P1>,
        p2: LiveData<P2>,
        p3: LiveData<P3>,
        p4: LiveData<P4>,
        p5: LiveData<P5>,
        p6: LiveData<P6>,
        p7: LiveData<P7>,
        p8: LiveData<P8>,
        p9: LiveData<P9>,
        p10: LiveData<P10>,
        p11: LiveData<P11>,
        p12: LiveData<P12>,
        p13: LiveData<P13>,
        p14: LiveData<P14>,
        p15: LiveData<P15>,
        p16: LiveData<P16>,
        func: ScriptFrame.(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16) -> CompletableFuture<R>
    ): AiyatsbusKether.Parser<R> {
        applyLiveData(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16)
        return AiyatsbusKether.Parser { frame ->
            applicative(
                p1.accept(frame),
                p2.accept(frame),
                p3.accept(frame),
                p4.accept(frame),
                p5.accept(frame),
                p6.accept(frame),
                p7.accept(frame),
                p8.accept(frame),
                p9.accept(frame),
                p10.accept(frame),
                p11.accept(frame),
                p12.accept(frame),
                p13.accept(frame),
                p14.accept(frame),
                p15.accept(frame),
                p16.accept(frame)
            ).thenCompose {
                func(
                    frame,
                    it.t1,
                    it.t2,
                    it.t3,
                    it.t4,
                    it.t5,
                    it.t6,
                    it.t7,
                    it.t8,
                    it.t9,
                    it.t10,
                    it.t11,
                    it.t12,
                    it.t13,
                    it.t14,
                    it.t15,
                    it.t16
                )
            }
        }
    }

}