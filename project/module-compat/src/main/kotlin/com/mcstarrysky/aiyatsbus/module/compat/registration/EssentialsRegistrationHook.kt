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
package com.mcstarrysky.aiyatsbus.module.compat.registration

import com.earth2me.essentials.Enchantments
import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.compat.EnchantRegistrationHook
import com.mcstarrysky.aiyatsbus.core.compat.EnchantRegistrationHooks
import org.bukkit.enchantments.Enchantment
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.library.reflex.Reflex.Companion.getProperty

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.compat.registration.EssentialsRegistrationHook
 *
 * @author mical
 * @since 2024/4/30 21:34
 */
class EssentialsRegistrationHook : EnchantRegistrationHook {

    private val fields = listOf("ENCHANTMENTS", "ALIASENCHANTMENTS")

    override fun getPluginName(): String = "Essentials"

    override fun register() {
        for (enchantment in Aiyatsbus.api().getEnchantmentManager().getEnchants().values) {
            for (field in fields) {
                Enchantments::class.java.getProperty<MutableMap<String, Enchantment>>(field)?.let {
                    it[enchantment.basicData.id] = enchantment.enchantment
                    it[enchantment.basicData.name] = enchantment.enchantment
                }
            }
        }
    }

    override fun unregister() {
        for (enchantment in Aiyatsbus.api().getEnchantmentManager().getEnchants().values) {
            for (field in fields) {
                Enchantments::class.java.getProperty<MutableMap<String, Enchantment>>(field)?.let {
                    it -= enchantment.basicData.id
                    it -= enchantment.basicData.name
                }
            }
        }
    }

    companion object {

        @Awake(LifeCycle.LOAD)
        fun init() {
            EnchantRegistrationHooks.registerHook(EssentialsRegistrationHook())
        }
    }
}