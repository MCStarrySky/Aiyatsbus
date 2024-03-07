package com.mcstarrysky.aiyatsbus.core.util

import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.PlayerUtils
 *
 * @author mical
 * @since 2024/3/7 18:18
 */
fun Player.placeBlock(placedBlock: Block, itemInHand: ItemStack = this.itemInHand): Boolean {
    val blockAgainst = placedBlock.getRelative(0, 1, 0)
    val event = BlockPlaceEvent(placedBlock, placedBlock.state, blockAgainst, itemInHand, this, true)
    return event.callEvent()
}