@file:Suppress("deprecation", "unchecked_cast")

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
import com.mcstarrysky.aiyatsbus.core.util.coerceDouble
import com.mcstarrysky.aiyatsbus.core.util.coerceFloat
import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveItemStack
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity.PropertyLivingEntity
 *
 * @author Lanscarlos
 * @since 2023-03-21 23:42
 */
@AiyatsbusProperty(
    id = "living-entity",
    bind = LivingEntity::class
)
class PropertyLivingEntity : AiyatsbusGenericProperty<LivingEntity>("living-entity") {

    override fun readProperty(instance: LivingEntity, key: String): OpenResult {
        val property: Any? = when (key) {
            "remainingAir", "remaining-air", "oxygen", "air" -> instance.remainingAir
            "maximumAir", "max-oxygen", "max-air" -> instance.maximumAir

            "killer" -> instance.killer
            "lastDamage", "last-damage", "last-dmg" -> instance.lastDamage
            "lastDamageCause","last-damage-cause", "last-dmg-cause" -> instance.lastDamageCause
            "noDamageTicks", "no-damage-ticks", "no-dmg-ticks", "no-damage-cooldown", "no-dmg-cd" -> instance.noDamageTicks

            "eyeLocation", "eye-location", "eye-loc" -> instance.eyeLocation
            "eyeHeight", "eye-height" -> instance.eyeHeight

            "hasPotion", "has-potion" -> instance.activePotionEffects.isNotEmpty()
            "hasAI", "has-ai" -> instance.hasAI()
            "isClimbing", "climbing" -> instance.isClimbing
            "isCollidable", "collidable" -> instance.isCollidable
            "isGliding", "gliding" -> instance.isGliding
            "isInvisible", "invisible" -> instance.isInvisible
            "isLeashed", "leashed" -> instance.isLeashed
            "isRiptiding", "riptiding" -> instance.isRiptiding
            "isSleeping", "sleeping" -> instance.isSleeping
            "isSwimming", "swimming" -> instance.isSwimming

            /* 其他属性 */
            "arrowCooldown", "arrow-cooldown", "arrow-cd"-> instance.arrowCooldown
            "arrowsInBody", "arrows-in-body", "arrows" -> instance.arrowsInBody
            "canPickupItems", "can-pickup-items" -> instance.canPickupItems
            "category" -> instance.category.name

            /* 装备栏相关属性 */
            "equipment" -> instance.equipment
            "armorContents", "armors" -> instance.equipment?.armorContents ?: List(6) { ItemStack(Material.AIR) }.toTypedArray()

            // 主手
            "mainHand", "main", "main-hand", "mainhand" -> instance.equipment?.itemInMainHand ?: ItemStack(Material.AIR)
            "itemInMainHandDropChance",
            "drop-chance-main", "chance-main",
            "drop-chance-main-hand", "chance-main-hand",
            "drop-chance-hand", "chance-hand" -> instance.equipment?.itemInMainHandDropChance ?: 0f

            // 副手
            "off", "off-hand" -> instance.equipment?.itemInOffHand ?: ItemStack(Material.AIR)
            "itemInOffHandDropChance",
            "drop-chance-off", "chance-off",
            "drop-chance-off-hand", "chance-off-hand" -> instance.equipment?.itemInOffHandDropChance ?: 0f

            // 头盔
            "helmet", "head" -> instance.equipment?.helmet ?: ItemStack(Material.AIR)
            "helmetDropChance",
            "drop-chance-helmet", "chance-helmet",
            "drop-chance-head", "chance-head" -> instance.equipment?.helmetDropChance ?: 0f

            // 胸甲
            "chestplate", "chest" -> instance.equipment?.chestplate ?: ItemStack(Material.AIR)
            "chestplateDropChance",
            "drop-chance-chestplate", "chance-chestplate",
            "drop-chance-chest", "chance-chest" -> instance.equipment?.chestplateDropChance ?: 0f

            // 护腿
            "leggings", "legs", "leg" -> instance.equipment?.leggings ?: ItemStack(Material.AIR)
            "leggingsDropChance",
            "drop-chance-leggings", "chance-leggings",
            "drop-chance-legs", "chance-legs",
            "drop-chance-leg", "chance-leg" -> instance.equipment?.leggingsDropChance ?: 0f

            // 护靴
            "boots", "feet", "foot" -> instance.equipment?.boots ?: ItemStack(Material.AIR)
            "bootsDropChance",
            "drop-chance-boots", "chance-boots",
            "drop-chance-feet", "chance-feet",
            "drop-chance-foot", "chance-foot" -> instance.equipment?.bootsDropChance ?: 0f

            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: LivingEntity, key: String, value: Any?): OpenResult {
        when (key) {

            "remainingAir", "remaining-air", "oxygen", "air" -> instance.remainingAir = value?.coerceInt() ?: return OpenResult.successful()
            "maximumAir", "max-oxygen", "max-air" -> instance.maximumAir = value?.coerceInt() ?: return OpenResult.successful()

            "lastDamage", "last-damage", "last-dmg" -> instance.lastDamage = value?.coerceDouble() ?: return OpenResult.successful()
            "lastDamageCause", "last-damage-cause", "last-dmg-cause" -> instance.lastDamageCause = value as? EntityDamageEvent ?: return OpenResult.successful()
            "noDamageTicks","no-damage-ticks", "no-dmg-ticks", "no-damage-cooldown", "no-dmg-cd" -> instance.noDamageTicks = value?.coerceInt() ?: return OpenResult.successful()

            "isCollidable", "collidable" -> instance.isCollidable = value?.coerceBoolean() ?: return OpenResult.successful()
            "isInvisible", "invisible" -> instance.isInvisible = value?.coerceBoolean() ?: return OpenResult.successful()
            "isSwimming", "swimming" -> instance.isSwimming = value?.coerceBoolean() ?: return OpenResult.successful()

            "arrowCooldown", "arrow-cooldown", "arrow-cd"-> instance.arrowCooldown = value?.coerceInt() ?: return OpenResult.successful()
            "arrowsInBody", "arrows-in-body", "arrows" -> instance.arrowsInBody = value?.coerceInt() ?: return OpenResult.successful()
            "canPickupItems", "can-pickup-items" -> instance.canPickupItems = value?.coerceBoolean() ?: return OpenResult.successful()

            "armorContents", "armors" -> instance.equipment?.armorContents = value as? Array<out ItemStack> ?: return OpenResult.successful()

            "itemInHand", "hand*" -> instance.equipment?.setItemInHand(value?.liveItemStack)
            "itemInHandDropChance", "drop-chance-hand*", "chance-hand*" -> instance.equipment?.itemInHandDropChance = value?.coerceFloat() ?: return OpenResult.successful()

            "mainHand", "main", "main-hand", "hand", "mainhand" -> instance.equipment?.setItemInMainHand(value?.liveItemStack)
            "itemInMainHandDropChance",
            "drop-chance-main", "chance-main",
            "drop-chance-main-hand", "chance-main-hand",
            "drop-chance-hand", "chance-hand" -> instance.equipment?.itemInMainHandDropChance = value?.coerceFloat() ?: return OpenResult.successful()

            "offHand", "off", "off-hand" -> instance.equipment?.setItemInOffHand(value?.liveItemStack)
            "itemInOffHandDropChance",
            "drop-chance-off", "chance-off",
            "drop-chance-off-hand", "chance-off-hand" -> instance.equipment?.itemInOffHandDropChance = value?.coerceFloat() ?: return OpenResult.successful()

            "helmet", "head" -> instance.equipment?.helmet = value?.liveItemStack
            "helmetDropChance",
            "drop-chance-helmet", "chance-helmet",
            "drop-chance-head", "chance-head" -> instance.equipment?.helmetDropChance = value?.coerceFloat() ?: return OpenResult.successful()

            "chestplate", "chest" -> instance.equipment?.chestplate = value?.liveItemStack
            "chestplateDropChance",
            "drop-chance-chestplate", "chance-chestplate",
            "drop-chance-chest", "chance-chest" -> instance.equipment?.chestplateDropChance = value?.coerceFloat() ?: return OpenResult.successful()

            "leggings", "legs", "leg" -> instance.equipment?.leggings = value?.liveItemStack
            "leggingsDropChance",
            "drop-chance-leggings", "chance-leggings",
            "drop-chance-legs", "chance-legs",
            "drop-chance-leg", "chance-leg" -> instance.equipment?.leggingsDropChance = value?.coerceFloat() ?: return OpenResult.successful()

            "boots", "feet", "foot" -> instance.equipment?.boots = value?.liveItemStack
            "bootsDropChance",
            "drop-chance-boots", "chance-boots",
            "drop-chance-feet", "chance-feet",
            "drop-chance-foot", "chance-foot" -> instance.equipment?.bootsDropChance = value?.coerceFloat() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}