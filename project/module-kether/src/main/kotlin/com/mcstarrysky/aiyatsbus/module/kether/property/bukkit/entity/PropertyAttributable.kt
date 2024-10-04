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

import com.mcstarrysky.aiyatsbus.core.util.coerceDouble
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.attribute.Attributable
import org.bukkit.attribute.Attribute
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity.PropertyAttributable
 *
 * @author yanshiqwq
 * @since 2024/4/4 12:09
 */
@AiyatsbusProperty(
    id = "attributable",
    bind = Attributable::class
)
class PropertyAttributable : AiyatsbusGenericProperty<Attributable>("attributable") {

    override fun readProperty(instance: Attributable, key: String): OpenResult {
        val property: Any? = when (key) {
            "baseArmor", "base-armor" -> instance.getAttribute(Attribute.GENERIC_ARMOR)?.baseValue ?: 0.0
            "armor" -> instance.getAttribute(Attribute.GENERIC_ARMOR)?.value ?: 0.0
            "baseArmorToughness", "base-armor-toughness" -> instance.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)?.baseValue ?: 0.0
            "armorToughness", "armor-toughness" -> instance.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)?.value ?: 0.0
            "baseAttackDamage", "base-attack-damage" -> instance.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue ?: 0.0
            "attackDamage", "attack-damage" -> instance.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value ?: 0.0
            "baseAttackKnockBack", "base-attack-knock-back" -> instance.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK)?.baseValue ?: 0.0
            "attackKnockBack", "attack-knock-back" -> instance.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK)?.value ?: 0.0
            "baseFlyingSpeed", "base-flying-speed" -> instance.getAttribute(Attribute.GENERIC_FLYING_SPEED)?.baseValue ?: 0.0
            "flyingSpeed", "flying-speed" -> instance.getAttribute(Attribute.GENERIC_FLYING_SPEED)?.value ?: 0.0
            "baseFollowRange", "base-follow-range" -> instance.getAttribute(Attribute.GENERIC_FOLLOW_RANGE)?.baseValue ?: 0.0
            "followRange", "follow-range" -> instance.getAttribute(Attribute.GENERIC_FOLLOW_RANGE)?.value ?: 0.0
            "baseLuck", "base-luck" -> instance.getAttribute(Attribute.GENERIC_LUCK)?.baseValue ?: 0.0
            "luck" -> instance.getAttribute(Attribute.GENERIC_LUCK)?.value ?: 0.0
            "baseMaxHealth", "base-max-health" -> instance.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue ?: 0.0
            "maxHealth", "max-health" -> instance.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 0.0
            "baseMovementSpeed", "base-movement-speed", "base-speed" -> instance.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue ?: 0.0
            "movementSpeed", "movement-speed", "speed" -> instance.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.value ?: 0.0
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Attributable, key: String, value: Any?): OpenResult {
        when (key) {
            "baseArmor", "base-armor" -> instance.getAttribute(Attribute.GENERIC_ARMOR)?.baseValue = value?.coerceDouble() ?: return OpenResult.successful()
            "baseArmorToughness", "base-armor-toughness" -> instance.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)?.baseValue = value?.coerceDouble() ?: return OpenResult.successful()
            "baseAttackDamage", "base-attack-damage" -> instance.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = value?.coerceDouble() ?: return OpenResult.successful()
            "baseAttackKnockBack", "base-attack-knock-back" -> instance.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK)?.baseValue = value?.coerceDouble() ?: return OpenResult.successful()
            "baseFlyingSpeed", "base-flying-speed" -> instance.getAttribute(Attribute.GENERIC_FLYING_SPEED)?.baseValue = value?.coerceDouble() ?: return OpenResult.successful()
            "baseFollowRange", "base-follow-range" -> instance.getAttribute(Attribute.GENERIC_FOLLOW_RANGE)?.baseValue = value?.coerceDouble() ?: return OpenResult.successful()
            "baseLuck", "base-luck" -> instance.getAttribute(Attribute.GENERIC_LUCK)?.baseValue = value?.coerceDouble() ?: return OpenResult.successful()
            "baseMaxHealth", "base-max-health" -> instance.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = value?.coerceDouble() ?: return OpenResult.successful()
            "baseMovementSpeed", "base-movement-speed", "base-speed" -> instance.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = value?.coerceDouble() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}