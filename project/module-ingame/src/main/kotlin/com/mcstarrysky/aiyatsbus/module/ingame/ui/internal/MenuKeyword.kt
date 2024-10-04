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
package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal

@Suppress("MemberVisibilityCanBePrivate")
data class MenuKeyword(val content: String, val indicator: String, val keyword: String) {

    constructor(content: String, delimiter: Char = DEFAULT_DELIMITER) : this(
        content,
        content.substringBefore(delimiter),
        content.substringAfter(delimiter, "")
    )

    companion object {
        var DEFAULT_DELIMITER: Char = ':'
        val EMPTY_KEYWORD: MenuKeyword by lazy { MenuKeyword("", "", "") }
        private val cached: MutableMap<String, MenuKeyword> = HashMap()

        fun of(content: String?, delimiter: Char = DEFAULT_DELIMITER): MenuKeyword {
            return content?.let { cached.computeIfAbsent(it) { _ -> MenuKeyword(it, delimiter) } } ?: EMPTY_KEYWORD
        }
    }
}
