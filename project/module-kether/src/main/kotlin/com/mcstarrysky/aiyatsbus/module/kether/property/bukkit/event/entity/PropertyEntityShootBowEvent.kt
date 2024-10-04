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
package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.entity

import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveEntity
import org.bukkit.event.entity.EntityShootBowEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.entity.PropertyEntityShootBowEvent
 *
 * @author mical
 * @since 2024/5/19 12:20
 */
@AiyatsbusProperty(
    id = "entity-shoot-bow-event",
    bind = EntityShootBowEvent::class
)
class PropertyEntityShootBowEvent : AiyatsbusGenericProperty<EntityShootBowEvent>("entity-shoot-bow-event") {

    override fun readProperty(instance: EntityShootBowEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "bow" -> instance.bow
            "hand" -> instance.hand
            "force" -> instance.force
            "consumable" -> instance.consumable
            "shouldConsumeItem", "should-consume-item" -> instance.shouldConsumeItem()
            "projectile" -> instance.projectile
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: EntityShootBowEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "shouldConsumeItem", "should-consume-item" -> instance.setConsumeItem(value?.coerceBoolean() ?: return OpenResult.successful())
            "projectile" -> instance.projectile = value?.liveEntity ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}