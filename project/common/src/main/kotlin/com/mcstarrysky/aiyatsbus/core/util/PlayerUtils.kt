package com.mcstarrysky.aiyatsbus.core.util

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import dev.lone.itemsadder.api.CustomBlock
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.PlayerUtils
 *
 * @author mical
 * @since 2024/3/26 18:08
 */

/**
 * 令玩家放置方块
 */
fun Player.placeBlock(placedBlock: Block, itemInHand: ItemStack = this.itemInHand): Boolean {
    val blockAgainst = placedBlock.getRelative(0, 1, 0)


    val event = BlockPlaceEvent(placedBlock, placedBlock.state, blockAgainst, itemInHand, this, true)
    return event.callEvent()
}

fun Player.doBreakBlock(block: Block) {
    try {
        block.mark("block-ignored")
        Aiyatsbus.api().getMinecraftAPI().breakBlock(this, block)
    } catch (ex: Throwable) {
        ex.printStackTrace()
    } finally {
        if (block.type != Material.AIR) {
            if (AiyatsbusSettings.supportItemsAdder && itemsAdderEnabled) {
                if (CustomBlock.byAlreadyPlaced(block) != null) {
                    CustomBlock.getLoot(block, inventory.itemInMainHand, true).forEach {
                        world.dropItem(block.location, it)
                    }
                    CustomBlock.remove(block.location)
                } else {
                    block.breakNaturally(inventory.itemInMainHand)
                }
            } else {
                block.breakNaturally(inventory.itemInMainHand)
            }
        }
        block.unmark("block-ignored")
    }
}
