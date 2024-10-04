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

import org.bukkit.inventory.ItemStack
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.data.ActionContext
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.data.BuildContext

interface MenuFunction {

    val name: String

    fun build(context: BuildContext): ItemStack

    fun handle(context: ActionContext)

}
@Suppress("MemberVisibilityCanBePrivate", "unused")
class MenuFunctionBuilder(name: String? = null, builder: MenuFunctionBuilder.() -> Unit) : MenuFunction {

    override var name: String = name ?: ""
        internal set

    private var builder: (BuildContext) -> ItemStack = { it.icon }
    private var handler: (ActionContext) -> Unit = {}

    init {
        builder()
    }

    fun onBuild(block: (BuildContext) -> ItemStack) {
        builder = block
    }

    fun onClick(block: (ActionContext) -> Unit) {
        handler = block
    }

    @Deprecated("Counterintuitive naming", ReplaceWith("onClick(block)"))
    fun onHandle(block: (ActionContext) -> Unit) = onClick(block)

    override fun build(context: BuildContext): ItemStack = builder(context)

    override fun handle(context: ActionContext) = handler(context)

}