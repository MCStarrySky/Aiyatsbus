/*
 * This file is part of Vulpecula, licensed under the MIT License.
 *
 *  Copyright (c) 2018 Bkm016
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity

import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.core.util.coerceFloat
import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.core.util.equippedItems
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.entity.Entity
import org.bukkit.util.Vector
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity.PropertyEntity
 *
 * @author Lanscarlos
 * @since 2023-03-21 23:52
 */
@AiyatsbusProperty(
    id = "entity",
    bind = Entity::class
)
class PropertyEntity : AiyatsbusGenericProperty<Entity>("entity") {

    override fun readProperty(instance: Entity, key: String): OpenResult {
        val property: Any? = when (key) {
            "boundingBox", "bounding-box", "box" -> instance.boundingBox
            "entityId", "entity-id", "id" -> instance.entityId
            "facing*" -> instance.facing.name
            "facing" -> instance.facing
            "fallDistance", "fall-distance" -> instance.fallDistance
            "fireTicks", "fire-ticks" -> instance.fireTicks
            "freezeTicks", "freeze-ticks" -> instance.freezeTicks
            "height" -> instance.height
            "lastDamageCause", "last-damage-cause" -> instance.lastDamageCause
            "location", "loc" -> instance.location
            "maxFireTicks", "max-fire-ticks" -> instance.maxFireTicks
            "maxFreezeTicks", "max-freeze-ticks" -> instance.maxFreezeTicks
            "passengers" -> instance.passengers
            "pistonMoveReaction", "piston-move-reaction", "piston-reaction" -> instance.pistonMoveReaction
            "portalCooldown", "portal-cooldown" -> instance.portalCooldown
            "pose" -> instance.pose
            "scoreboardTags", "scoreboard-tags" -> instance.scoreboardTags
            "server" -> instance.server
            "spawnCategory", "spawn-category" -> instance.spawnCategory
            "ticksLived", "ticks-lived" -> instance.ticksLived
            "type*" -> instance.type
            "type" -> instance.type
            "uniqueId", "unique-id", "uuid" -> instance.uniqueId.toString()
            "vehicle" -> instance.vehicle
            "velocity" -> instance.velocity
            "width" -> instance.width
            "world" -> instance.world

            "gravity" -> instance.hasGravity()
            "isCustomNameVisible", "custom-name-visible" -> instance.isCustomNameVisible
            "isDead", "dead" -> instance.isDead
            "isEmpty", "empty" -> instance.isEmpty
            "isFrozen", "frozen" -> instance.isFrozen
            "isGlowing", "glowing" -> instance.isGlowing
            "isInsideVehicle", "inside-vehicle" -> instance.isInsideVehicle
            "isInvulnerable", "invulnerable" -> instance.isInvulnerable
            "isInWater", "in-water" -> instance.isInWater
            "isInRain", "in-rain" -> instance.isInRain
            "isInBubbleColumn", "in-bubble-column", "in-bubble" -> instance.isInBubbleColumn
            "isInLava", "in-lava" -> instance.isInLava
            "isInPowderedSnow", "in-powdered-snow" -> instance.isInPowderedSnow
            "isOnGround", "on-ground" -> instance.isOnGround
            "isPersistent", "persistent" -> instance.isPersistent
            "isSilent", "silent" -> instance.isSilent
            "isValid", "valid" -> instance.isValid
            "isVisualFire", "visual-fire" -> instance.isVisualFire

            "isFromMobSpawner", "from-mob-spawner" -> instance.fromMobSpawner()

            "isMoving", "moving" -> !instance.velocity.isZero

            "equippedItem", "equipped-items" -> instance.equippedItems // Aiyatsbus
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Entity, key: String, value: Any?): OpenResult {
        when (key) {
            "isCustomNameVisible", "custom-name-visible" -> instance.isCustomNameVisible = value?.coerceBoolean() ?: return OpenResult.successful()
            "fallDistance", "fall-distance" -> instance.fallDistance = value?.coerceFloat() ?: return OpenResult.successful()
            "fireTicks", "fire-ticks" -> instance.fireTicks = value?.coerceInt() ?: return OpenResult.successful()
            "freezeTicks", "freeze-ticks" -> instance.freezeTicks = value?.coerceInt() ?: return OpenResult.successful()
            "isGlowing", "glowing" -> instance.isGlowing = value?.coerceBoolean() ?: return OpenResult.successful()
            "gravity" -> instance.setGravity(value?.coerceBoolean() ?: return OpenResult.successful())
            "isInvulnerable", "invulnerable" -> instance.isInvulnerable = value?.coerceBoolean() ?: return OpenResult.successful()
            "isPersistent", "persistent" -> instance.isPersistent = value?.coerceBoolean() ?: return OpenResult.successful()
            "portalCooldown", "portal-cooldown" -> instance.portalCooldown = value?.coerceInt() ?: return OpenResult.successful()
            "rotation" -> {
                val pair = value as? Pair<*, *> ?: return OpenResult.successful()
                instance.setRotation(
                    pair.first.coerceFloat(0f),
                    pair.second.coerceFloat(0f)
                )
            }
            "isSilent", "silent" -> instance.isSilent = value?.coerceBoolean() ?: return OpenResult.successful()
            "ticks-lived" -> instance.ticksLived = value?.coerceInt() ?: return OpenResult.successful()
            "velocity" -> instance.velocity = value as? Vector ?: Vector(0, 0, 0)
            "isVisualFire", "visual-fire" -> instance.isVisualFire = value?.coerceBoolean() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}