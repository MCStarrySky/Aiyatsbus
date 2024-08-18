package com.mcstarrysky.aiyatsbus.impl.registration.v12100_paper

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentBase
import net.minecraft.world.item.enchantment.Enchantment

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.registration.v12100_paper.EnchantmentHelper
 *
 * @author mical
 * @since 2024/8/17 14:56
 */
object EnchantmentHelper {

    fun createCraftEnchantment(enchant: AiyatsbusEnchantmentBase, nms: Enchantment): Any {
        return AiyatsbusCraftEnchantment(enchant, nms)
    }
}