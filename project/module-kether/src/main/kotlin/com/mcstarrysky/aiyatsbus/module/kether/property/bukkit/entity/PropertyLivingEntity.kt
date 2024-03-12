@file:Suppress("deprecation", "unchecked_cast")

package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity

import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.core.util.coerceDouble
import com.mcstarrysky.aiyatsbus.core.util.coerceFloat
import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveItemStack
import org.bukkit.Material
import org.bukkit.attribute.Attribute
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

            "health" -> instance.health
            "max-health" -> instance.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue ?: 0.0

            "remaining-air", "oxygen", "air" -> instance.remainingAir
            "max-oxygen", "max-air" -> instance.maximumAir

            "killer" -> instance.killer
            "last-damage", "last-dmg" -> instance.lastDamage
            "last-damage-cause", "last-dmg-cause" -> instance.lastDamageCause
            "no-damage-ticks", "no-dmg-ticks", "no-damage-cooldown", "no-dmg-cd" -> instance.noDamageTicks

            "eye-location", "eye-loc" -> instance.eyeLocation
            "eye-height" -> instance.eyeHeight

            "has-potion" -> instance.activePotionEffects.isNotEmpty()
            "has-ai" -> instance.hasAI()
            "climbing" -> instance.isClimbing
            "collidable" -> instance.isCollidable
            "gliding" -> instance.isGliding
            "invisible" -> instance.isInvisible
            "leashed" -> instance.isLeashed
            "riptiding" -> instance.isRiptiding
            "sleeping" -> instance.isSleeping
            "swimming" -> instance.isSwimming

            /* 其他属性 */
            "arrow-cooldown", "arrow-cd"-> instance.arrowCooldown
            "arrows-in-body", "arrows" -> instance.arrowsInBody
            "can-pickup-items" -> instance.canPickupItems
            "category" -> instance.category.name

            /* 装备栏相关属性 */
            "equipment" -> instance.equipment
            "armorContents", "armors" -> {
                instance.equipment?.armorContents ?: List(6) { ItemStack(Material.AIR) }.toTypedArray()
            }

            // 主手
            "main", "main-hand", "hand" -> instance.equipment?.itemInMainHand ?: ItemStack(Material.AIR)
            "drop-chance-main", "chance-main",
            "drop-chance-main-hand", "chance-main-hand",
            "drop-chance-hand", "chance-hand" -> instance.equipment?.itemInMainHandDropChance ?: 0f

            // 副手
            "off", "off-hand" -> instance.equipment?.itemInOffHand ?: ItemStack(Material.AIR)
            "drop-chance-off", "chance-off",
            "drop-chance-off-hand", "chance-off-hand" -> instance.equipment?.itemInOffHandDropChance ?: 0f

            // 头盔
            "helmet", "head" -> instance.equipment?.helmet ?: ItemStack(Material.AIR)
            "drop-chance-helmet", "chance-helmet",
            "drop-chance-head", "chance-head" -> instance.equipment?.helmetDropChance ?: 0f

            // 胸甲
            "chestplate", "chest" -> instance.equipment?.chestplate ?: ItemStack(Material.AIR)
            "drop-chance-chestplate", "chance-chestplate",
            "drop-chance-chest", "chance-chest" -> instance.equipment?.chestplateDropChance ?: 0f

            // 护腿
            "leggings", "legs", "leg" -> instance.equipment?.leggings ?: ItemStack(Material.AIR)
            "drop-chance-leggings", "chance-leggings",
            "drop-chance-legs", "chance-legs",
            "drop-chance-leg", "chance-leg" -> instance.equipment?.leggingsDropChance ?: 0f

            // 护靴
            "boots", "feet", "foot" -> instance.equipment?.boots ?: ItemStack(Material.AIR)
            "drop-chance-boots", "chance-boots",
            "drop-chance-feet", "chance-feet",
            "drop-chance-foot", "chance-foot" -> instance.equipment?.bootsDropChance ?: 0f

            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: LivingEntity, key: String, value: Any?): OpenResult {
        when (key) {

            "health" -> {
                instance.health = value?.coerceDouble() ?: return OpenResult.successful()
            }
            "max-health" -> {
                instance.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue =
                    value?.coerceDouble() ?: return OpenResult.successful()
            }

            "remaining-air", "oxygen", "air" -> {
                instance.remainingAir = value?.coerceInt() ?: return OpenResult.successful()
            }
            "max-oxygen", "max-air" -> {
                instance.maximumAir = value?.coerceInt() ?: return OpenResult.successful()
            }

            "last-damage", "last-dmg" -> {
                instance.lastDamage = value?.coerceDouble() ?: return OpenResult.successful()
            }
            "last-damage-cause", "last-dmg-cause" -> {
                instance.lastDamageCause = value as? EntityDamageEvent ?: return OpenResult.successful()
            }
            "no-damage-ticks", "no-dmg-ticks", "no-damage-cooldown", "no-dmg-cd" -> {
                instance.noDamageTicks = value?.coerceInt() ?: return OpenResult.successful()
            }

            "arrow-cooldown", "arrow-cd"-> {
                instance.arrowCooldown = value?.coerceInt() ?: return OpenResult.successful()
            }
            "arrows-in-body", "arrows" -> {
                instance.arrowsInBody = value?.coerceInt() ?: return OpenResult.successful()
            }
            "can-pickup-items" -> {
                instance.canPickupItems = value?.coerceBoolean() ?: return OpenResult.successful()
            }

            "armorContents", "armors" -> {
                instance.equipment?.armorContents = value as? Array<out ItemStack> ?: return OpenResult.successful()
            }

            "hand*" -> {
                instance.equipment?.setItemInHand(value?.liveItemStack)
            }
            "drop-chance-hand*", "chance-hand*" -> {
                instance.equipment?.itemInHandDropChance = value?.coerceFloat() ?: return OpenResult.successful()
            }

            "main", "main-hand", "hand" -> {
                instance.equipment?.setItemInMainHand(value?.liveItemStack)
            }
            "drop-chance-main", "chance-main",
            "drop-chance-main-hand", "chance-main-hand",
            "drop-chance-hand", "chance-hand" -> {
                instance.equipment?.itemInMainHandDropChance = value?.coerceFloat() ?: return OpenResult.successful()
            }

            "off", "off-hand" -> {
                instance.equipment?.setItemInOffHand(value?.liveItemStack)
            }
            "drop-chance-off", "chance-off",
            "drop-chance-off-hand", "chance-off-hand" -> {
                instance.equipment?.itemInOffHandDropChance = value?.coerceFloat() ?: return OpenResult.successful()
            }

            "helmet", "head" -> {
                instance.equipment?.helmet = value?.liveItemStack
            }
            "drop-chance-helmet", "chance-helmet",
            "drop-chance-head", "chance-head" -> {
                instance.equipment?.helmetDropChance = value?.coerceFloat() ?: return OpenResult.successful()
            }

            "chestplate", "chest" -> {
                instance.equipment?.chestplate = value?.liveItemStack
            }
            "drop-chance-chestplate", "chance-chestplate",
            "drop-chance-chest", "chance-chest" -> {
                instance.equipment?.chestplateDropChance = value?.coerceFloat() ?: return OpenResult.successful()
            }

            "leggings", "legs", "leg" -> {
                instance.equipment?.chestplate = value?.liveItemStack
            }
            "drop-chance-leggings", "chance-leggings",
            "drop-chance-legs", "chance-legs",
            "drop-chance-leg", "chance-leg" -> {
                instance.equipment?.leggingsDropChance = value?.coerceFloat() ?: return OpenResult.successful()
            }

            "boots", "feet", "foot" -> {
                instance.equipment?.boots = value?.liveItemStack
            }
            "drop-chance-boots", "chance-boots",
            "drop-chance-feet", "chance-feet",
            "drop-chance-foot", "chance-foot" -> {
                instance.equipment?.bootsDropChance = value?.coerceFloat() ?: return OpenResult.successful()
            }
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}