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
package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal

import com.mcstarrysky.aiyatsbus.core.util.calcToInt
import com.mcstarrysky.aiyatsbus.core.util.isNull
import org.bukkit.entity.Player
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.advance.ShapeConfiguration
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.advance.TemplateConfiguration
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.textured
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import taboolib.module.ui.type.Chest
import taboolib.module.ui.type.PageableChest
import taboolib.platform.util.modifyMeta

fun Chest.setSlots(
    shape: ShapeConfiguration,
    templates: TemplateConfiguration,
    key: String,
    elements: List<Any?> = listOf(),
    vararg args: Pair<String, Any>
) {
    var tot = 0
    shape[key].forEach { slot ->
        val map = args.toMap().mapValues {
            val parts = it.value.toString().split("=")
            when (parts[0]) {
                "expression" -> parts[1].calcToInt("tot" to "$tot")
                "element" -> elements.getOrNull(parts.getOrNull(1)?.calcToInt("tot" to "$tot") ?: tot)
                else -> it.value
            }
        }
        set(slot, templates(key, slot, 0, false, "Fallback") { this += map })
        onClick(slot) { templates[it.rawSlot]?.handle(this, it) { this += map } }
        tot++
    }
}

fun Chest.load(
    shape: ShapeConfiguration,
    templates: TemplateConfiguration,
    player: Player,
    vararg ignored: String
) {
    val notAuto = ignored.toMutableList() + "Back"
    onBuild(async = true) { _, inventory ->
        shape.all(*notAuto.toTypedArray()) { slot, index, item, _ ->
            inventory.setItem(slot, item(slot, index))
        }
    }

    if (shape["Back", true].isNotEmpty())
        setSlots(shape, templates, "Back", listOf(), "player" to player)

    onClick { event ->
        event.isCancelled = true
        if (event.rawSlot in shape && notAuto.none { shape[it, true].contains(event.rawSlot) }) {
            templates[event.rawSlot]?.handle(this, event, "player" to player)
        }
    }
}

fun <T> PageableChest<T>.pages(
    shape: ShapeConfiguration,
    templates: TemplateConfiguration
) {
    shape["Previous"].forEach { slot -> setPreviousPage(slot) { it, _ -> templates("Previous", slot, it) } }
    shape["Next"].forEach { slot -> setNextPage(slot) { it, _ -> templates("Next", slot, it) } }
}

fun ItemStack.skull(skull: String?): ItemStack {
    skull ?: return this
    if (isNull) return this
    if (itemMeta !is SkullMeta) return this
    return if (skull.length <= 20) modifyMeta<SkullMeta> { owner = skull }
    else textured(skull)
}