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
package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.advance

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuSection
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature.FunctionalFeature
import taboolib.common.platform.function.info

@Suppress("MemberVisibilityCanBePrivate", "unused")
class KeywordConfiguration(val holder: MenuConfiguration) {

    private val keywords: BiMap<String, Char> by lazy {
        HashBiMap.create<String, Char>().apply {
            holder.templates.forEach { char, (_, _, features) ->
                for (extra in features[FunctionalFeature.name] ?: return@forEach) {
                    runCatching {
                        val keyword = FunctionalFeature.keyword(extra)
                        require(keyword !in this) { "存在重复的 Functional 关键词: $keyword@${this[keyword]}, $char" }
                        this[keyword] = char
                    }.onFailure {
                        MenuSection.TEMPLATE incorrect ("获取字符 $char 对应模板的 Functional 关键词时遇到错误" to it)
                    }
                }
            }

            if (holder.isDebug) {
                info("已加载的关键词: $this")
            }
        }
    }

    operator fun get(slot: Int): String? = get(holder.shape[slot])

    operator fun get(char: Char): String? = keywords.inverse()[char]

    operator fun get(keyword: String): Char? = keywords[keyword]

    fun require(keyword: String): Char = get(keyword) ?: (MenuSection.TEMPLATE incorrect "缺少与 Functional 关键词 $keyword 相关联的模版")

}