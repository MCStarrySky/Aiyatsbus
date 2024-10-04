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