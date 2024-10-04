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
package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.block

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.block.BlockFace
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.block.PropertyBlockFace
 *
 * @author mical
 * @since 2024/4/5 21:12
 */
@AiyatsbusProperty(
    id = "block-face",
    bind = BlockFace::class
)
class PropertyBlockFace : AiyatsbusGenericProperty<BlockFace>("block-face") {

    override fun readProperty(instance: BlockFace, key: String): OpenResult {
        val property: Any? = when (key) {
            "name" -> instance.name
            "direction" -> instance.direction
            "modX", "mod-x" -> instance.modX
            "modY", "mod-y" -> instance.modY
            "modZ", "mod-z" -> instance.modZ
            "oppositeFace", "opposite-face", "opposite" -> instance.oppositeFace
            "isCartesian", "is-cartesian", "cartesian" -> instance.isCartesian
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockFace, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}