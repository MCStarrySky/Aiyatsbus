package com.mcstarrysky.aiyatsbus.core.registration

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentBase
import org.bukkit.enchantments.Enchantment

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.registration.AiyatsbusEnchantmentRegisterer
 *
 * @author mical
 * @since 2024/2/17 14:59
 */
interface AiyatsbusEnchantmentRegisterer {

    fun register(enchant: AiyatsbusEnchantmentBase) : Enchantment

    fun unregister(enchant: AiyatsbusEnchantment)
}