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
package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity

import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.core.util.coerceDouble
import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.entity.AbstractArrow
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity.PropertyAbstractArrow
 *
 * @author yanshiqwq
 * @since 2024/4/4 13:54
 */
@AiyatsbusProperty(
    id = "abstract-arrow",
    bind = AbstractArrow::class
)
class PropertyAbstractArrow : AiyatsbusGenericProperty<AbstractArrow>("abstract-arrow") {

    override fun readProperty(instance: AbstractArrow, key: String): OpenResult {
        val property: Any? = when (key) {
            "knockbackStrength", "knockback-strength", "knockback" -> instance.knockbackStrength
            "baseDamage", "base-damage", "damage" -> instance.damage
            "pierceLevel", "pierce-level" -> instance.pierceLevel
            "isCritical", "critical", "crit" -> instance.isCritical
            "isInBlock", "in-block" -> instance.isInBlock
            "attachedBlock" -> if (instance.isInBlock) instance.attachedBlock else "NULL"
            "isCanPickup" -> instance.pickupStatus == AbstractArrow.PickupStatus.ALLOWED
            "isCannotPickup" -> instance.pickupStatus == AbstractArrow.PickupStatus.DISALLOWED
            "isCreativeOnlyPickup" -> instance.pickupStatus == AbstractArrow.PickupStatus.CREATIVE_ONLY
            "isShotFromCrossbow", "from-crossbow" -> instance.isShotFromCrossbow
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: AbstractArrow, key: String, value: Any?): OpenResult {
        when (key) {
            "knockbackStrength", "knockback-strength", "knockback" -> instance.knockbackStrength = value?.coerceInt() ?: return OpenResult.successful()
            "baseDamage", "base-damage", "damage" -> instance.damage = value?.coerceDouble() ?: return OpenResult.successful()
            "pierceLevel", "pierce-level" -> instance.pierceLevel = value?.coerceInt() ?: return OpenResult.successful()
            "isCritical", "critical", "crit" -> instance.isCritical = value?.coerceBoolean() ?: return OpenResult.successful()
            "isShotFromCrossbow", "from-crossbow" -> instance.isShotFromCrossbow = value?.coerceBoolean() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}