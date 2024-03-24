package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature

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