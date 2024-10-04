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
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveItemStack
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.util.Vector
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBlockDispenseEvent
 *
 * @author yanshiqwq
 * @since 2024/4/6 15:46
 */
@AiyatsbusProperty(
    id = "block-dispense-event",
    bind = BlockDispenseEvent::class
)
class PropertyBlockDispenseEvent : AiyatsbusGenericProperty<BlockDispenseEvent>("block-dispense-event"){

    override fun readProperty(instance: BlockDispenseEvent, key: String): OpenResult {
        val property: Any? = when(key) {
            "item" -> instance.item
            "velocity" -> instance.velocity
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockDispenseEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "item" -> instance.item = value?.liveItemStack ?: return OpenResult.successful()
            "velocity" -> instance.velocity = value as Vector? ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}