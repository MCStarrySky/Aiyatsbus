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
package com.mcstarrysky.aiyatsbus.module.kether.action.game

import com.mcstarrysky.aiyatsbus.core.util.BlockUtils
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack
import taboolib.module.kether.KetherParser
import taboolib.module.kether.ParserHolder.option
import taboolib.module.kether.combinationParser

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionWorld
 *
 * @author mical
 * @since 2024/7/14 12:42
 */
object ActionWorld {

    @KetherParser(["get-vein"], shared = true)
    fun getVeinParser() = combinationParser {
        it.group(type<Block>(), command("max", then = int()).option()).apply(it) { block, amount ->
            now { BlockUtils.getVein(block, amount) }
        }
    }

    @KetherParser(["strike-lightning", "lightning"], shared = true)
    fun strikeLightningParser() = combinationParser {
        it.group(type<Location>()).apply(it) { loc ->
            now {
                loc.world.strikeLightning(loc)
            }
        }
    }

    @KetherParser(["create-explosion"], shared = true)
    fun createExplosionParser() = combinationParser {
        it.group(type<Location>(), float(), command("by", then = type<Entity>()).option(), command("fire", then = bool()).option(), command("break", then = bool()).option()).apply(it) { location, float, entity, fire, break0 ->
            now { location.world.createExplosion(entity, location, float, fire ?: true, break0 ?: true) }
        }
    }

    @KetherParser(["drop-item"], shared = true)
    fun actionDropItem() = combinationParser {
        it.group(type<ItemStack>(), command("at", then = type<Location>()), command("naturally", then = bool()).option()).apply(it) { item, loc, naturally ->
            now {
                if (naturally == true) {
                    loc.world.dropItemNaturally(loc, item)
                } else {
                    loc.world.dropItem(loc, item)
                }
            }
        }
    }
}