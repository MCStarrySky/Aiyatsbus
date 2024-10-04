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

import com.mcstarrysky.aiyatsbus.core.compat.AntiGriefChecker
import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.core.util.placeBlock
import org.bukkit.FluidCollisionMode
import org.bukkit.Material
import org.bukkit.block.data.Ageable
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.takeItem

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.operation.Plant
 *
 * @author mical
 * @since 2024/3/11 22:23
 */
object Plant {

    val seedsMap = linkedMapOf(
        "BEETROOT_SEEDS" to "BEETROOTS",
        "MELON_SEEDS" to "MELON_STEM",
        "PUMPKIN_SEEDS" to "PUMPKIN_STEM",
        "TORCHFLOWER_SEEDS" to "TORCHFLOWER_CROP",
        "WHEAT_SEEDS" to "WHEAT",
        "CARROT" to "CARROTS",
        "POTATO" to "POTATOES"
    )

    fun getSeed(player: Player, seeds: String?): Material? {
        if (seeds == "ALL") return player.inventory.contents.find { seedsMap.containsKey(it?.type?.name) }?.type
        try {
            val type = seeds?.let { Material.valueOf(it) } ?: return null
            if (player.inventory.containsAtLeast(ItemStack(type), 1)) return type
        } catch (ignored: Exception) {
        }
        return null
    }

    fun plant(args: List<Any?>?) {
        plant(args?.get(0) as Player, args[1].coerceInt(), args[2].toString())
    }

    private fun plant(player: Player, sideLength: Int, seeds: String?) {
        if (sideLength <= 1) return

        val block = player.rayTraceBlocks(6.0, FluidCollisionMode.NEVER)?.hitBlock ?: return
        val loc = block.location
        val type = getSeed(player, seeds) ?: return

        val down = -sideLength / 2
        val up = sideLength / 2 - if (sideLength % 2 == 0) 1 else 0

        for (x in down until up + 1) {
            for (z in down until up + 1) {
                val current = loc.clone().add(x.toDouble(), 0.0, z.toDouble())
                if (current.block.type != Material.FARMLAND) continue
                val planted = current.clone().add(0.0, 1.0, 0.0).block
                if (!AntiGriefChecker.canBreak(player, planted.location))
                    continue
                if (planted.type != Material.AIR) continue // 防止左右手打架
                if (player.placeBlock(planted, ItemStack(type, 1))) {
                    planted.type = Material.valueOf(seedsMap[type.name]!!)
                    val data = planted.blockData as Ageable
                    data.age = 0
                    planted.blockData = data
                    player.inventory.takeItem(1) { it.type == type }
                }
            }
        }
    }
}