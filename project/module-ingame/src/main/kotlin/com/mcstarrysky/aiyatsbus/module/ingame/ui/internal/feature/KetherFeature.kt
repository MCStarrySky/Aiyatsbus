package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature

import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.asList
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.MenuFeature
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.data.ActionContext
import taboolib.common.platform.function.adaptPlayer
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions
import taboolib.module.kether.runKether

@Suppress("unused")
object KetherFeature : MenuFeature() {

    override val name: String = "Kether"

    override fun handle(context: ActionContext) {
        val (_, extra, _, event, _) = context
        val scripts = extra.asList<String>("scripts") ?: return
        runKether {
            KetherShell.eval(scripts, ScriptOptions.new {
                sender(adaptPlayer(event.clicker))
            })
        }
    }

}