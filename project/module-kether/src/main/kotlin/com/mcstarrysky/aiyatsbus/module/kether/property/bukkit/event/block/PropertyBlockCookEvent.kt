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
import org.bukkit.event.block.BlockCookEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyBlockCookEvent
 *
 * @author yanshiqwq
 * @since 2024/4/1 20:50
 */
@AiyatsbusProperty(
    id = "block-cook-event",
    bind = BlockCookEvent::class
)
class PropertyBlockCookEvent : AiyatsbusGenericProperty<BlockCookEvent>("block-cook-event") {

    override fun readProperty(instance: BlockCookEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "source" -> instance.source
            "result" -> instance.result
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockCookEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "result" -> instance.result = value?.liveItemStack ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}