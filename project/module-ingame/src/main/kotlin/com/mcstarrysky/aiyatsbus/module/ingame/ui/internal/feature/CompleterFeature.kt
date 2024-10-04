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
package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature

import com.mcstarrysky.aiyatsbus.core.util.VariableReaders
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.MenuFeature
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.data.ActionContext
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.*
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.registry.VariableProviders
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.submit
import taboolib.module.chat.colored
import taboolib.module.kether.KetherShell
import taboolib.platform.util.nextChat

@Suppress("unused")
object CompleterFeature : MenuFeature() {

    override val name: String = "Completer"

    override fun handle(context: ActionContext) {
        val (_, extra, _, event, _) = context
        val mode = extra.valueOrNull<String>("mode")?.let {
            requireNotNull(it.enumOf()) { "未知的 Completer 模式: $it" }
        } ?: Mode.COMMAND
        val contexts = extra.asList<String>("contexts") ?: return
        val message = extra.value<String>("message")
        val user = event.clicker

        user.closeInventory()
        user.sendMessage(message.colored())
        user.nextChat { input ->
            when (mode) {
                Mode.COMMAND -> submit {
                    contexts
                        .map {
                            VariableReaders.BRACES.replaceNested(it) {
                                if (this == "input") {
                                    input
                                } else {
                                    VariableProviders[this]?.produce(context) ?: ""
                                }
                            }
                        }
                        .forEach(user::performCommand)
                }

                Mode.KETHER -> KetherShell.eval(contexts, sender = adaptPlayer(user)) {
                    set("input", input)
                    set("args", context.args)
                }
            }
        }
    }

    enum class Mode {
        COMMAND, KETHER;
    }

}