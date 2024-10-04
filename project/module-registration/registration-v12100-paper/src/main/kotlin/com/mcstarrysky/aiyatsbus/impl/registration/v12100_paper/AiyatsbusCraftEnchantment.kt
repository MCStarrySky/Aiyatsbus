@file:Suppress("NO_CAST_NEEDED")

/*
 * This file is part of EcoEnchants, licensed under the GPL-3.0 License.
 *
 *  Copyright (C) 2024 Auxilor
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
package com.mcstarrysky.aiyatsbus.impl.registration.v12100_paper

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentBase
import com.mcstarrysky.aiyatsbus.core.util.legacyToAdventure
import net.kyori.adventure.text.Component
import net.minecraft.world.item.enchantment.Enchantment
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.enchantments.CraftEnchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.inventory.ItemStack

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.registration.modern.AiyatsbusCraftEnchantment
 *
 * @author mical
 * @since 2024/2/17 17:15
 */
class AiyatsbusCraftEnchantment(
    private val enchant: AiyatsbusEnchantmentBase,
    nmsEnchantment: Enchantment
) : CraftEnchantment(enchant.enchantmentKey, nmsEnchantment), AiyatsbusEnchantment by enchant {

    init {
        enchant.enchantment = this
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return enchant.canEnchantItem(item)
    }

    override fun conflictsWith(other: org.bukkit.enchantments.Enchantment): Boolean {
        return enchant.conflictsWith(other)
    }

    override fun translationKey(): String {
        return enchant.basicData.id
    }

    override fun getName(): String {
        return enchant.basicData.id.uppercase()
    }

    override fun getMaxLevel(): Int {
        return enchant.basicData.maxLevel
    }

    override fun getStartLevel(): Int = 1

    override fun getItemTarget(): EnchantmentTarget = EnchantmentTarget.ALL

    override fun isTreasure(): Boolean {
        return enchant.alternativeData.isTreasure
    }

    override fun isCursed(): Boolean {
        return enchant.alternativeData.isCursed
    }

    override fun displayName(level: Int): Component {
        return enchant.displayName(level).legacyToAdventure()
    }

    override fun isTradeable(): Boolean {
        return enchant.alternativeData.isTradeable
    }

    override fun isDiscoverable(): Boolean {
        return enchant.alternativeData.isDiscoverable
    }

    override fun getMinModifiedCost(level: Int): Int {
        return 0
    }

    override fun getMaxModifiedCost(level: Int): Int {
        return 0
    }

    override fun getDamageIncrease(level: Int, entityCategory: EntityCategory): Float = 0.0f

    override fun equals(other: Any?): Boolean {
        // 不这样转换过不了编译, 项目依赖配置有问题
        return other is AiyatsbusEnchantment && (this.enchantmentKey as NamespacedKey) == other.enchantmentKey
    }

    override fun hashCode(): Int {
        // 不这样转换过不了编译, 项目依赖配置有问题
        return (this.enchantmentKey as NamespacedKey).hashCode()
    }

    override fun toString(): String {
        return "AiyatsbusCraftEnchantment(key=$key)"
    }
}