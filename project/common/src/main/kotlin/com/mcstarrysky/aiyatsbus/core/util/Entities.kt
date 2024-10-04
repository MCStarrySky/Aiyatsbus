/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.mcstarrysky.aiyatsbus.core.util

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import com.mcstarrysky.aiyatsbus.core.compat.NPCChecker
import dev.lone.itemsadder.api.CustomBlock
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import kotlin.math.PI

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.EntityUtils
 *
 * @author mical
 * @since 2024/7/14 15:30
 */
fun Entity?.checkIfIsNPC(): Boolean {
    return NPCChecker.checkIfIsNPC(this ?: return false)
}

fun LivingEntity.isBehind(entity: LivingEntity): Boolean {
    if (world.name != entity.world.name) {
        return false
    }
    val directionSelf = entity.location.clone().subtract(location).toVector()
    directionSelf.setY(0)
    val direction = entity.location.direction
    direction.setY(0)
    return directionSelf.angle(direction) < PI / 3
}

val Entity.equippedItems: Map<EquipmentSlot, ItemStack>
    get() = if (this is LivingEntity) EquipmentSlot.values()
        .associateWith { (equipment?.getItem(it) ?: ItemStack(Material.AIR)) } else emptyMap()

/**
 * 对 LivingEntity 造成真实伤害, 插件和原版无法减伤
 */
fun LivingEntity.realDamage(amount: Double, who: Entity? = null) {
    health = maxOf(0.1, health - amount + 0.5)
    damage(0.5, who)
    if (isDead) health = 0.0
}

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