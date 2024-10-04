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

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyPlayer
import taboolib.common.util.Location
import taboolib.common.util.Vector
import taboolib.library.kether.LoadError
import taboolib.library.xseries.XMaterial
import taboolib.module.kether.ScriptFrame
import taboolib.platform.util.buildItem
import taboolib.platform.util.toProxyLocation
import java.awt.Color
import java.util.concurrent.CompletableFuture

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.AiyatsbusKether
 *
 * @author Lanscarlos
 * @since 2023-02-26 21:01
 */
open class LiveData<T>(
    val func: AiyatsbusReader.() -> AiyatsbusKether.Action<T>
) {

    var trim: Array<out String> = emptyArray()
    var expect: Array<out String> = emptyArray()
    lateinit var action: AiyatsbusKether.Action<T>

    open fun isAccepted(): Boolean {
        return ::action.isInitialized
    }

    open fun accept(reader: AiyatsbusReader): LiveData<T> {
        if (isAccepted()) return this
        if (trim.isNotEmpty()) reader.expectToken(*trim)
        if (expect.isNotEmpty() && !reader.expectToken(*expect)) {
            throw LoadError.NOT_MATCH.create("[${expect.joinToString(", ")}]", reader.peekToken())
        }
        action = func(reader)
        return this
    }

    open fun accept(frame: ScriptFrame): CompletableFuture<T> {
        return action.run(frame)
    }

    fun trim(vararg expect: String): LiveData<T> {
        trim = expect
        return this
    }

    fun expect(vararg expect: String): LiveData<T> {
        this.expect = expect
        return this
    }

    fun optional(vararg expect: String): LiveData<T?> {
        return if (expect.isEmpty()) {
            LiveData optional@{
                this@LiveData.accept(reader = this)
                AiyatsbusKether.Action { frame ->
                    this@LiveData.accept(frame).thenApply { it }
                }
            }
        } else {
            LiveData optional@{
                if (this.expectToken(*expect)) {
                    this@LiveData.accept(reader = this)
                    AiyatsbusKether.Action { frame ->
                        this@LiveData.accept(frame).thenApply { it }
                    }
                } else {
                    AiyatsbusKether.Action { CompletableFuture.completedFuture(null) }
                }
            }
        }
    }

    fun optional(vararg expect: String, def: T): LiveData<T> {
        return if (expect.isNotEmpty()) {
            LiveData optional@{
                if (this.expectToken(*expect)) {
                    this@LiveData.accept(reader = this)
                    AiyatsbusKether.Action { frame ->
                        this@LiveData.accept(frame).thenApply { it }
                    }
                } else {
                    AiyatsbusKether.Action { CompletableFuture.completedFuture(def) }
                }
            }
        } else this
    }

    fun <R> map(func: (T) -> R): LiveData<R> {
        return LiveData map@{
            this@LiveData.accept(reader = this)
            AiyatsbusKether.Action { frame ->
                this@LiveData.accept(frame).thenApply(func)
            }
        }
    }

    fun <R> union(other: LiveData<R>): LiveData<Pair<T, R>> {
        return LiveData union@{
            this@LiveData.accept(reader = this)
            other.accept(reader = this)
            AiyatsbusKether.Action { frame ->
                applicative(
                    this@LiveData.accept(frame),
                    other.accept(frame)
                ).thenApply {
                    it.t1 to it.t2
                }
            }
        }
    }

    companion object {

        fun <T> point(value: T): LiveData<T> {
            return LiveData {
                AiyatsbusKether.Action {
                    CompletableFuture.completedFuture(value)
                }
            }
        }

        fun <T> readerOf(func: (AiyatsbusReader) -> T): LiveData<T> {
            return LiveData {
                val value = func(this)
                AiyatsbusKether.Action {
                    CompletableFuture.completedFuture(value)
                }
            }
        }

        fun <T> frameOf(func: (ScriptFrame) -> T): LiveData<T> {
            return LiveData {
                AiyatsbusKether.Action { frame ->
                    CompletableFuture.completedFuture(func(frame))
                }
            }
        }

        fun <T> frameBy(func: ScriptFrame.(Any?) -> T): LiveData<T> {
            return LiveData {
                val action = this.readAction()
                AiyatsbusKether.Action { frame ->
                    frame.newFrame(action).run<Any?>().thenApply { func(frame, it) }
                }
            }
        }

        val Any.liveBoolean: Boolean?
            get() {
                return when (this) {
                    is Boolean -> this
                    "true", "yes" -> true
                    "false", "no" -> false
                    is Number -> this.toInt() != 0
                    is String -> this.toBoolean()
                    else -> null
                }
            }

        val Any.liveShort: Short?
            get() {
                return when (this) {
                    is Number -> this.toShort()
                    is String -> this.toShortOrNull()
                    else -> null
                }
            }

        val Any.liveInt: Int?
            get() {
                return when (this) {
                    is Number -> this.toInt()
                    is String -> this.toIntOrNull()
                    else -> null
                }
            }

        val Any.liveLong: Long?
            get() {
                return when (this) {
                    is Number -> this.toLong()
                    is String -> this.toLongOrNull()
                    else -> null
                }
            }

        val Any.liveFloat: Float?
            get() {
                return when (this) {
                    is Number -> this.toFloat()
                    is String -> this.toFloatOrNull()
                    else -> null
                }
            }

        val Any.liveDouble: Double?
            get() {
                return when (this) {
                    is Number -> this.toDouble()
                    is String -> this.toDoubleOrNull()
                    else -> null
                }
            }

        val Any.liveStringList: List<String>?
            get() {
                return when (this) {
                    is String -> listOf(this)
                    is Array<*> -> {
                        this.mapNotNull { el -> el?.toString() }
                    }

                    is Collection<*> -> {
                        this.mapNotNull { el -> el?.toString() }
                    }

                    else -> null
                }
            }

        val Any.liveVector: Vector?
            get() = when (this) {
                is Vector -> this
                is org.bukkit.util.Vector -> Vector(this.x, this.y, this.z)
                is Location -> this.direction
                is org.bukkit.Location -> this.toProxyLocation().direction
                is String -> {
                    when {
                        this == "x" -> Vector(1.0, 0.0, 0.0)
                        this == "y" -> Vector(0.0, 1.0, 0.0)
                        this == "z" -> Vector(0.0, 0.0, 1.0)
                        this.matches("-?\\d+(\\.\\d+)?".toRegex()) -> {
                            // 数字
                            val number = this.toDouble()
                            Vector(number, number, number)
                        }

                        this.matches("^-?\\d+(\\.\\d+)?,-?\\d+(\\.\\d+)?,-?\\d+(\\.\\d+)?\$".toRegex()) -> {
                            // x,y,z
                            val demand = this.split(",").map { it.toDouble() }
                            Vector(demand[0], demand[1], demand[2])
                        }

                        else -> null
                    }
                }

                else -> null
            }

        val Any.liveLocation: Location?
            get() = when (this) {
                is Location -> this
                is org.bukkit.Location -> this.toProxyLocation()
                is ProxyPlayer -> this.location
                is Entity -> this.location.toProxyLocation()
                is Vector -> Location(null, this.x, this.y, this.z)
                is org.bukkit.util.Vector -> Location(null, this.x, this.y, this.z)
                is String -> {
                    if (this.matches("-?\\d+(\\.\\d+)?,-?\\d+(\\.\\d+)?,-?\\d+(\\.\\d+)?(,-?\\d+(\\.\\d+)?,-?\\d+(\\.\\d+)?)?".toRegex())) {
                        /*
                        * x,y,z
                        * x,y,z,yaw,pitch
                        * */
                        val demand = this.split(",")
                        Location(
                            null,
                            demand[0].toDouble(), demand[1].toDouble(), demand[2].toDouble(),
                            demand.getOrNull(3)?.toFloatOrNull() ?: 0f,
                            demand.getOrNull(4)?.toFloatOrNull() ?: 0f
                        )
                    } else if (this.matches("^[A-Za-z0-9_\\- \\u4e00-\\u9fa5]+,-?\\d+(\\.\\d+)?,-?\\d+(\\.\\d+)?,-?\\d+(\\.\\d+)?(,-?\\d+(\\.\\d+)?,-?\\d+(\\.\\d+)?)?\$".toRegex())) {
                        /*
                        * world,x,y,z
                        * world,x,y,z,yaw,pitch
                        * */
                        val demand = this.split(",")
                        Location(
                            demand[0], demand[1].toDouble(), demand[2].toDouble(), demand[3].toDouble(),
                            demand.getOrNull(4)?.toFloatOrNull() ?: 0f,
                            demand.getOrNull(5)?.toFloatOrNull() ?: 0f
                        )
                    } else null
                }

                else -> null
            }

        val Any.liveColor: Color?
            get() {
                return when (this) {
                    is Color -> this
                    is org.bukkit.Color -> Color(this.red, this.green, this.blue)
                    is String -> {
                        if (this.startsWith('#') && this.matches("^#([A-Fa-f0-9]{8}|[A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})\$".toRegex())) {
                            // hex
                            Color.decode(this)
                        } else if (this.matches("^\\d+-\\d+-\\d+(-\\d+)?\$".toRegex())) {
                            val demand = this.split("-").map { it.toInt().coerceIn(0, 255) }
                            if (demand.size == 4) {
                                // r-g-b-a
                                Color(demand[0], demand[1], demand[2], demand[3])
                            } else {
                                Color(demand[0], demand[1], demand[2])
                            }
                        } else {
                            val rgb = this.toIntOrNull() ?: return null
                            Color(rgb)
                        }
                    }

                    else -> null
                }
            }

        val Any.liveEntity: Entity?
            get() {
                return when (this) {
                    is Entity -> this
                    is OfflinePlayer -> this.player
                    is ProxyPlayer -> this.castSafely()
                    is String -> Bukkit.getPlayerExact(this)
                    else -> null
                }
            }

        val Any.livePlayer: Player?
            get() {
                return when (this) {
                    is Player -> this
                    is OfflinePlayer -> this.player
                    is ProxyPlayer -> this.castSafely()
                    is String -> Bukkit.getPlayerExact(this)
                    else -> null
                }
            }

        val Any.liveItemStack: ItemStack?
            get() {
                return when (this) {
                    is ItemStack -> this
                    is Item -> this.itemStack
                    is String -> {
                        val material = XMaterial.matchXMaterial(this.uppercase()).let { mat ->
                            if (mat.isPresent) mat.get() else return null
                        }
                        buildItem(material)
                    }

                    else -> null
                }
            }

        val Any.liveInventory: Inventory?
            get() {
                return when (this) {
                    is Inventory -> this
                    is HumanEntity -> this.inventory
                    is ProxyPlayer -> this.castSafely<Player>()?.inventory
                    is String -> {
                        Bukkit.getPlayerExact(this)?.inventory
                    }

                    else -> null
                }
            }

    }
}