package com.mcstarrysky.aiyatsbus.impl.registration.legacy

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentBase
import com.mcstarrysky.aiyatsbus.core.util.legacyToAdventure
import io.papermc.paper.enchantments.EnchantmentRarity
import net.kyori.adventure.text.Component
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.registration.legacy.LegacyCraftEnchantment
 *
 * @author mical
 * @since 2024/2/17 15:01
 */
class LegacyCraftEnchantment(
    private val enchant: AiyatsbusEnchantmentBase
) : Enchantment(enchant.enchantmentKey), AiyatsbusEnchantment by enchant {

    init {
        enchant.enchantment = this
    }

    override fun getName(): String = enchant.basicData.id.uppercase()

    override fun getMaxLevel(): Int = enchant.basicData.maxLevel

    override fun getStartLevel(): Int = 1

    override fun getItemTarget(): EnchantmentTarget = EnchantmentTarget.ALL

    override fun isTreasure(): Boolean = enchant.alternativeData.isTreasure

    override fun isCursed(): Boolean = enchant.alternativeData.isCursed

    override fun conflictsWith(other: Enchantment): Boolean = enchant.conflictsWith(other)

    override fun canEnchantItem(item: ItemStack): Boolean = true

    override fun displayName(level: Int): Component {
        return enchant.displayName(level).legacyToAdventure()
    }

    override fun isTradeable(): Boolean = enchant.alternativeData.isTradeable

    override fun isDiscoverable(): Boolean = enchant.alternativeData.isDiscoverable

    override fun getMinModifiedCost(level: Int): Int {
        return 0
    }

    override fun getMaxModifiedCost(level: Int): Int {
        return 0
    }

    override fun getRarity(): EnchantmentRarity = EnchantmentRarity.VERY_RARE

    override fun getDamageIncrease(level: Int, entityCategory: EntityCategory): Float = 0.0f

    override fun getActiveSlots(): MutableSet<EquipmentSlot> = mutableSetOf()

    override fun equals(other: Any?): Boolean {
        return other is AiyatsbusEnchantment && this.enchantmentKey == other.enchantmentKey
    }

    override fun hashCode(): Int {
        return this.enchantmentKey.hashCode()
    }

    override fun translationKey(): String = enchant.basicData.id
}