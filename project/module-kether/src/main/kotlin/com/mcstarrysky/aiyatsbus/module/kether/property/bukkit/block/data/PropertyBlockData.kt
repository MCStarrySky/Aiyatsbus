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
package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.block.data

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.block.data.BlockData
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.block.data.BlockData
 *
 * @author mical
 * @since 2024/3/14 21:32
 */
@AiyatsbusProperty(
    id = "block-data",
    bind = BlockData::class
)
class PropertyBlockData : AiyatsbusGenericProperty<BlockData>("block-data") {

    override fun readProperty(instance: BlockData, key: String): OpenResult {
        val property: Any? = when (key) {
            "isOccluding", "is-occluding", "occluding" -> instance.isOccluding
            "isRandomlyTicked", "is-randomly-ticked", "randomly-ticked" -> instance.isRandomlyTicked
            "asString", "as-string" -> instance.asString
            "soundGroup", "sound-group" -> instance.soundGroup
            "lightEmission", "light-emission" -> instance.lightEmission
            "requiresCorrectToolForDrops", "check-tool", "drops-by-tool" -> instance.requiresCorrectToolForDrops()
            "pistonMoveReaction", "piston-move-reaction", "piston-reaction" -> instance.pistonMoveReaction
            "placementMaterial", "placement-material" -> instance.placementMaterial
            else -> OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockData, key: String, value: Any?): OpenResult {
        // TODO
        return OpenResult.failed()
    }
}