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
package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config

internal enum class MenuSection(val display: String, vararg val paths: String) {
    DEBUG("调试模式", "debug", "dev"),
    TITLE("标题", "title", "heading"),
    SHAPE("布局", "shape", "layout"),
    TEMPLATE("模板", "templates", "template", "items", "item");

    val formatted: String by lazy { "$display(${paths.joinToString("/")})" }

    fun missing(): Nothing = throw IllegalArgumentException("GUI 配置缺失或无效: $formatted")

    infix fun incorrect(reason: String): Nothing = throw IllegalArgumentException("GUI 配置 $formatted 不正确: $reason")

    infix fun incorrect(context: Pair<String, Throwable>) {
        val (reason, cause) = context
        IllegalArgumentException("GUI 配置 $formatted 不正确: $reason", cause).printStackTrace()
    }
}