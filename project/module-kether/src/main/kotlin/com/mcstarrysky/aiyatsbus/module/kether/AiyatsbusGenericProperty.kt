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

import taboolib.common.OpenResult
import taboolib.module.kether.ScriptProperty
import taboolib.module.kether.action.ActionProperty

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
 *
 * @author Lanscarlos
 * @since 2023-03-19 22:17
 */
abstract class AiyatsbusGenericProperty<T : Any>(
    id: String
) : ScriptProperty<T>("aiyatsbus.$id.operator") {

    abstract fun readProperty(instance: T, key: String): OpenResult

    abstract fun writeProperty(instance: T, key: String, value: Any?): OpenResult

    override fun read(instance: T, key: String): OpenResult {
        return if (key.contains('.')) {
            val path = key.split('.')
            // 连续读取
            readPropertyDeep(instance, path)
        } else {
            readProperty(instance, key)
        }
    }

    override fun write(instance: T, key: String, value: Any?): OpenResult {
        return if (key.contains('.')) {
            val path = key.split('.')
            // 连续读取写入
            writePropertyDeep(instance, path, value)
        } else {
            writeProperty(instance, key, value)
        }
    }

    /**
     * 连续读取
     * */
    protected open fun readPropertyDeep(instance: T, path: List<String>): OpenResult {
        return when {
            path.isEmpty() -> OpenResult.failed()
            path.size == 1 -> readProperty(instance, path.first())
            path.size == 2 -> {
                val cache = readProperty(instance, path.first())
                if (cache.isFailed) return OpenResult.failed()
                readGenericProperty(cache.get() ?: return OpenResult.successful(null), path.last())
            }
            else -> {
                var cache = readProperty(instance, path.first())
                if (cache.isFailed) return OpenResult.failed()

                for (i in 1 until path.lastIndex) {
                    // 遍历除最后一个外所有节点
                    cache = readGenericProperty(cache.get() ?: return OpenResult.successful(null), path[i])
                }

                if (cache.isFailed) return OpenResult.failed()
                readGenericProperty(cache.get() ?: return OpenResult.successful(null), path.last())
            }
        }
    }

    /**
     * 连续读取写入
     * */
    protected open fun writePropertyDeep(instance: T, path: List<String>, value: Any?): OpenResult {
        return when {
            path.isEmpty() -> OpenResult.failed()
            path.size == 1 -> writeProperty(instance, path.first(), value)
            path.size == 2 -> {
                val cache = readProperty(instance, path.first())
                if (cache.isFailed) return OpenResult.failed()
                writeGenericProperty(cache.get() ?: return OpenResult.successful(), path.last(), value)
            }
            else -> {
                var cache = readProperty(instance, path.first())
                if (cache.isFailed) return OpenResult.failed()

                for (i in 1 until path.lastIndex) {
                    // 遍历除最后一个外所有节点
                    cache = readGenericProperty(cache.get() ?: return OpenResult.successful(), path[i])
                }

                if (cache.isFailed) return OpenResult.failed()
                writeGenericProperty(cache.get() ?: return OpenResult.successful(), path.last(), value)
            }
        }
    }

    /**
     * 读取泛型属性
     * */
    protected open fun <R: Any> readGenericProperty(instance: R, key: String): OpenResult {

        // 查询所有相关属性
        ActionProperty.getScriptProperty(instance).filterIsInstance<ScriptProperty<Any?>>().forEach {

            // 排除自己，防止无限调用
            if (this.id == it.id) return@forEach

            val result = if (it is AiyatsbusGenericProperty) {
                // 这里不再使用泛型写入，防止套娃
                it.readProperty(instance, key)
            } else {
                it.read(instance, key)
            }
            if (result.isSuccessful) return result
        }
        return OpenResult.failed()
    }

    /**
     * 写入泛型属性
     * */
    protected open fun <R: Any> writeGenericProperty(instance: R, key: String, value: Any?): OpenResult {

        // 查询所有相关属性
        ActionProperty.getScriptProperty(instance).filterIsInstance<ScriptProperty<Any?>>().forEach {

            // 排除自己，防止无限调用
            if (this.id == it.id) return@forEach

            val result = if (it is AiyatsbusGenericProperty) {
                // 这里不再使用泛型写入，防止套娃
                it.writeProperty(instance, key, value)
            } else {
                it.write(instance, key, value)
            }
            if (result.isSuccessful) return result
        }
        return OpenResult.failed()
    }

    fun OpenResult.get(): Any? {
        return if (this.isSuccessful) this.value else null
    }
}