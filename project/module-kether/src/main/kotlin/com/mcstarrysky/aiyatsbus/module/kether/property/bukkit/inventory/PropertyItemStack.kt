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
package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.inventory

import com.mcstarrysky.aiyatsbus.core.belongedTargets
import com.mcstarrysky.aiyatsbus.core.util.*
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common.OpenResult
import taboolib.module.nms.getI18nName

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.property
 *
 * @author Lanscarlos
 * @since 2023-03-22 13:59
 */
@AiyatsbusProperty(
    id = "itemstack",
    bind = ItemStack::class
)
class PropertyItemStack : AiyatsbusGenericProperty<ItemStack>("itemstack") {

    override fun readProperty(instance: ItemStack, key: String): OpenResult {
        val property: Any? = when (key) {
            "type", "material", "mat" -> instance.type.name
            "name" -> instance.itemMeta?.displayName ?: instance.getI18nName()
            "has-name" -> instance.itemMeta?.hasDisplayName()

            "lore" -> instance.itemMeta?.lore
            "has-lore" -> instance.itemMeta?.hasLore()

            "amount", "amt" -> instance.amount
            "max-amount", "max-amt", "max-size" -> instance.maxStackSize

            "durability", "dura" -> instance.dura
//            "damage", "dmg" -> instance.damage
            "max-durability", "max-dura" -> instance.maxDurability

            "enchants" -> instance.itemMeta?.enchants
            "enchantments" -> instance.enchantments
            "has-enchantments", "has-enchants" -> instance.itemMeta?.hasEnchants()

            "flags" -> instance.itemMeta?.itemFlags
            "has-flags" -> instance.itemMeta?.itemFlags?.isNotEmpty() ?: false

            "custom-model-data", "custom-model", "model-data", "model" -> instance.itemMeta?.customModelData
            "has-custom-model-data", "has-custom-model", "has-model-data", "has-model" -> instance.itemMeta?.hasCustomModelData()

//            "attribute-modifiers", "modifiers" -> instance.itemMeta?.attributeModifiers
            "has-attribute-modifiers", "has-modifiers" -> instance.itemMeta?.hasAttributeModifiers()

            "unbreakable" -> instance.itemMeta?.isUnbreakable

            "item-meta", "meta" -> instance.itemMeta
            "has-meta" -> instance.hasItemMeta()

            /* 材质相关属性 */
            "is-solid" -> instance.type.isSolid
            "is-item" -> instance.type.isItem
            "is-record" -> instance.type.isRecord
            "is-occluding" -> instance.type.isOccluding
            "is-interactable" -> instance.type.isInteractable
            "is-fuel" -> instance.type.isFuel
            "is-flammable" -> instance.type.isFlammable
            "is-edible" -> instance.type.isEdible
            "is-burnable" -> instance.type.isBurnable
            "is-block" -> instance.type.isBlock
            "is-air" -> instance.type.isAir
            "has-gravity" -> instance.type.hasGravity()
            "slipperiness" -> instance.type.slipperiness
            "hardness" -> instance.type.hardness
            "slot" -> instance.type.equipmentSlot.name
            "blast-resistance", "resistance" -> instance.type.blastResistance
            "creative-category", "category" -> instance.type.creativeCategory

            /* 其他不常用属性 */
            "clone" -> instance.clone()
            "data" -> instance.data
            "serialize" -> instance.serialize()
            "to-string", "string" -> instance.toString()
            "localized-name", "localized" -> instance.itemMeta?.localizedName
            "has-localized-name", "has-localized" -> instance.itemMeta?.hasLocalizedName()

            "belongingTargets", "belonging-targets" -> instance.type.belongedTargets.map { it.id } // Aiyatsbus
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: ItemStack, key: String, value: Any?): OpenResult {
        when (key) {
            "type" -> {
                instance.type = when (value) {
                    is Material -> value
                    is String -> Material.values().firstOrNull { value.equals(it.name, true) } ?: instance.type
                    else -> instance.type
                }
            }
            "name" -> {
                val meta = instance.itemMeta
                meta?.setDisplayName(value?.toString())
                instance.itemMeta = meta
            }
            "lore" -> {
                val meta = instance.itemMeta
                meta?.lore = when (value) {
                    is String -> listOf(value)
                    is Array<*> -> value.mapNotNull { it?.toString() }
                    is Collection<*> -> value.mapNotNull { it?.toString() }
                    else -> null
                }
                instance.itemMeta = meta
            }
            "amount", "amt" -> {
                instance.amount = value.coerceInt(instance.amount)
            }
            "durability", "dura" -> {
                instance.dura = value?.coerceInt() ?: return OpenResult.successful()
            }
//            "damage", "dmg" -> {
//                instance.damage = value?.coerceInt() ?: return OpenResult.successful()
//            }
            "item-meta", "meta" -> {
                instance.itemMeta = value as? ItemMeta
            }

            "custom-model-data", "custom-model", "model-data", "model" -> {
                val meta = instance.itemMeta
                meta?.setCustomModelData(value.coerceInt(meta.customModelData))
                instance.itemMeta = meta
            }
            "unbreakable", "unbreak" -> {
                instance.itemMeta?.isUnbreakable = value.coerceBoolean(instance.itemMeta?.isUnbreakable)
            }
            "localized-name", "localized" -> {
                instance.itemMeta?.setLocalizedName(value?.toString())
            }
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }

}