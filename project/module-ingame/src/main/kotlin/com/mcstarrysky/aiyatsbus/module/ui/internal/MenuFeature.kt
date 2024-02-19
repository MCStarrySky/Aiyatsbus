package com.mcstarrysky.aiyatsbus.module.ui.internal

import org.bukkit.inventory.ItemStack
import com.mcstarrysky.aiyatsbus.module.ui.internal.data.ActionContext
import com.mcstarrysky.aiyatsbus.module.ui.internal.data.BuildContext

abstract class MenuFeature {

    abstract val name: String

    open fun build(context: BuildContext): ItemStack = context.icon

    open fun handle(context: ActionContext) {}

}