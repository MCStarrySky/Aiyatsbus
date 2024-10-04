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
package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature.util

import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.data.ActionContext

interface MenuOpener {

    val name: String

    fun open(context: ActionContext)

    operator fun invoke(context: ActionContext) = open(context)

}

@Suppress("unused")
class MenuOpenerBuilder(name: String? = null, builder: MenuOpenerBuilder.() -> Unit) : MenuOpener {

    override var name: String = name ?: ""
        internal set

    private var handler: (ActionContext) -> Unit = {
        throw NotImplementedError("未调用 onOpen 方法实现该打开方式")
    }

    init {
        builder()
    }

    fun onOpen(block: (ActionContext) -> Unit) {
        handler = block
    }

    override fun open(context: ActionContext) = handler(context)

}