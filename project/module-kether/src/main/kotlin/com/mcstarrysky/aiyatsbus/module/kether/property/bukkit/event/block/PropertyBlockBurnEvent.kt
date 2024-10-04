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
package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.block.BlockBurnEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBlockBurnEvent
 *
 * @author yanshiqwq
 * @since 2024/4/1 20:38
 */
@AiyatsbusProperty(
    id = "block-burn-event",
    bind = BlockBurnEvent::class
)
class PropertyBlockBurnEvent : AiyatsbusGenericProperty<BlockBurnEvent>("block-burn-event") {

    override fun readProperty(instance: BlockBurnEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "ignitingBlock", "igniting-block" -> instance.ignitingBlock
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockBurnEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}