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

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.game.ActionEntityEquipment
 *
 * @author mical
 * @since 2024/7/14 13:37
 */
object ActionEntityEquipment {

    @KetherParser(["equip-set-item"], shared = true)
    fun equipSetItem() = combinationParser {
        it.group(
            type<ItemStack?>(),
            command("in", then = any()),
            command("to", "on", then = type<Entity>())
        ).apply(it) { item, slot, entity ->
            now { (entity as? LivingEntity)?.equipment?.setItem(slot as? EquipmentSlot ?: EquipmentSlot.valueOf(slot.toString()), item) }
        }
    }

    @KetherParser(["equip-get-item"], shared = true)
    fun equipGetItem() = combinationParser {
        it.group(
            any(),
            command("from", "on", then = type<Entity>())
        ).apply(it) { slot, entity ->
            now { (entity as? LivingEntity)?.equipment?.getItem(slot as? EquipmentSlot ?: EquipmentSlot.valueOf(slot.toString())) }
        }
    }
}