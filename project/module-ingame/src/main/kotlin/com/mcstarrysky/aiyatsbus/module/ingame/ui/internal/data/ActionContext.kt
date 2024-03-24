package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.data

import org.bukkit.entity.Player
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration
import taboolib.module.ui.ClickEvent
import taboolib.module.ui.Menu

data class ActionContext(
    val config: MenuConfiguration,
    val extra: Map<String, Any?>,
    val menu: Menu,
    val event: ClickEvent,
    val args: Map<String, Any?>
) {
    val clicker: Player
        get() = event.clicker
}
