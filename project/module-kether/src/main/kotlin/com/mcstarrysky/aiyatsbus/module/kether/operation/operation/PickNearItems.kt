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
package com.mcstarrysky.aiyatsbus.module.kether.operation.operation

import com.mcstarrysky.aiyatsbus.core.compat.GuardItemChecker
import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.core.util.coerceLong
import com.mcstarrysky.aiyatsbus.core.util.isNull
import org.bukkit.Location
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.submit

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.operation.operation.PickupItem
 *
 * @author mical
 * @since 2024/8/19 10:22
 */
object PickNearItems {

    fun pickNearItems(args: List<Any?>?) {
        pickNearItems(
            args?.get(0) as Player,
            args[1] as Location,
            args[2].coerceInt(),
            args[3].coerceLong()
        )
    }

    fun pickNearItems(player: Player, location: Location, checkRadius: Int, checkDelay: Long) {
        submit(delay = checkDelay) {
            for (item in location.getNearbyEntitiesByType(Item::class.java, checkRadius.toDouble())) {
                if (GuardItemChecker.checkIsGuardItem(item.itemStack)) continue
                if (canFitItem(player, item.itemStack)) item.remove()
            }
        }
    }

    private fun canFitItem(player: Player, item: ItemStack): Boolean {
        val inventory = player.inventory
        var emptySlots = 0
        var availableSpace = 0

        for (stack in inventory.storageContents) {
            if (stack.isNull) {
                emptySlots++
                availableSpace += item.maxStackSize
                continue
            }

            if (stack!!.isSimilar(item)) {
                availableSpace += item.maxStackSize - stack.amount
            }
        }
        val canFit = emptySlots > 0 || availableSpace >= item.amount

        if (canFit) {
            inventory.addItem(item)
        }
        return canFit
    }
}