package com.mcstarrysky.aiyatsbus.core.event

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.event.AiyatsbusPrepareAnvilEvent
 *
 * @author mical
 * @since 2024/5/3 16:02
 */
class AiyatsbusPrepareAnvilEvent(val left: ItemStack, val right: ItemStack?, var result: ItemStack?, var name: String?, val player: Player) : BukkitProxyEvent() {

    override val allowCancelled: Boolean = true
}