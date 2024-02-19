package com.mcstarrysky.aiyatsbus.module.ui.internal.feature

import com.mcstarrysky.aiyatsbus.module.ui.internal.MenuFeature
import com.mcstarrysky.aiyatsbus.module.ui.internal.data.ActionContext
import taboolib.common.platform.function.submit

@Suppress("unused")
object CloseFeature : MenuFeature() {

    override val name: String = "Close"

    override fun handle(context: ActionContext) {
        submit {
            context.event.clicker.closeInventory()
        }
    }

}