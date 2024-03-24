package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature

import org.bukkit.inventory.ItemStack
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.MenuFeature
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.data.ActionContext
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.data.BuildContext
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.value
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.registry.MenuFunctions

object FunctionalFeature : MenuFeature() {

    override val name: String = "Functional"

    override fun build(context: BuildContext): ItemStack = MenuFunctions[keyword(context.extra)]?.build(context) ?: context.icon

    override fun handle(context: ActionContext) {
        MenuFunctions[keyword(context.extra)]?.handle(context)
    }

    fun keyword(extra: Map<*, *>): String = extra.value("keyword")

}