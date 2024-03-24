package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal

import org.bukkit.inventory.ItemStack
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.data.ActionContext
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.data.BuildContext

abstract class MenuFeature {

    abstract val name: String

    open fun build(context: BuildContext): ItemStack = context.icon

    open fun handle(context: ActionContext) {}

}