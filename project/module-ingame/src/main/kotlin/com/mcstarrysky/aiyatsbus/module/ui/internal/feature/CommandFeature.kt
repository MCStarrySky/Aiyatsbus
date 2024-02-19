package com.mcstarrysky.aiyatsbus.module.ui.internal.feature

import com.mcstarrysky.aiyatsbus.module.ui.internal.MenuFeature
import com.mcstarrysky.aiyatsbus.module.ui.internal.data.ActionContext
import com.mcstarrysky.aiyatsbus.module.ui.internal.registry.VariableProviders
import com.mcstarrysky.aiyatsbus.module.ui.internal.function.VariableReaders
import com.mcstarrysky.aiyatsbus.module.ui.internal.function.asList

@Suppress("unused")
object CommandFeature : MenuFeature() {

    override val name: String = "Command"

    override fun handle(context: ActionContext) {
        val (_, extra, _, event, _) = context
        val commands = extra.asList<String>("commands") ?: return
        val user = event.clicker
        commands.map {
            VariableReaders.BRACES.replaceNested(it) {
                VariableProviders[this]?.produce(context) ?: ""
            }
        }.forEach(user::performCommand)
    }

}