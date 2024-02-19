package com.mcstarrysky.aiyatsbus.impl.registration.modern

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.util.toAdventureComponent
import io.papermc.paper.adventure.PaperAdventure
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.craftbukkit.v1_20_R3.enchantments.CraftEnchantment
import java.util.Objects

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.registration.modern.VanillaAiyatsbusEnchantment
 *
 * @author mical
 * @since 2024/2/17 16:49
 */
class VanillaAiyatsbusEnchantment(
    val id: String
) : Enchantment(
    Rarity.VERY_RARE,
    EnchantmentCategory.VANISHABLE,
    emptyArray()
) {

    private val enchant: AiyatsbusEnchantment?
        get() = Aiyatsbus.api().getEnchantmentManager().getByID(id)

    override fun getSlotItems(entity: LivingEntity): MutableMap<EquipmentSlot, ItemStack> = mutableMapOf()

    override fun getMinLevel(): Int = 1

    override fun getMaxLevel(): Int = enchant?.basicData?.maxLevel ?: 1

    override fun isCurse(): Boolean = false

    override fun isDiscoverable(): Boolean = false

    override fun isTradeable(): Boolean = false

    override fun isTreasureOnly(): Boolean = true

    override fun checkCompatibility(other: Enchantment): Boolean {
        return enchant?.conflictsWith(CraftEnchantment.minecraftToBukkit(other)) == false
    }

    override fun getFullname(level: Int): Component {
        return if (enchant != null) PaperAdventure.asVanilla((enchant?.displayName(level) ?: "").toAdventureComponent()) else super.getFullname(level)
    }

    override fun toString(): String {
        return "VanillaAiyatsbusEnchantment(id='$id')"
    }

    override fun equals(other: Any?): Boolean {
        return other is VanillaAiyatsbusEnchantment && other.id == this.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}