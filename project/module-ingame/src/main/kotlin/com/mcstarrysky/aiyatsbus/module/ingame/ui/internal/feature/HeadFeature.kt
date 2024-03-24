package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature

import org.bukkit.inventory.ItemStack
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.MenuFeature
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.data.BuildContext
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.textured
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.value
import taboolib.library.xseries.XMaterial

@Suppress("unused")
object HeadFeature : MenuFeature() {

    override val name: String = "Head"

    override fun build(context: BuildContext): ItemStack {
        val (_, extra, _, _, icon, _) = context
        if (!XMaterial.PLAYER_HEAD.isSimilar(icon)) {
            return icon
        }
        return icon textured extra.value("texture")
    }

}