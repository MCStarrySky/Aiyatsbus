package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.data

import org.bukkit.inventory.ItemStack
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration

data class BuildContext(
    val config: MenuConfiguration,
    val extra: Map<String, Any?>,
    val slot: Int,
    val index: Int,
    val icon: ItemStack,
    val args: Map<String, Any?>
)
