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

import com.mcstarrysky.aiyatsbus.core.compat.EnchantRegistrationHook
import com.mcstarrysky.aiyatsbus.core.compat.EnchantRegistrationHooks
import net.Zrips.CMILib.Enchants.CMIEnchantment
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.compat.registration.CMIRegistrationHook
 *
 * @author mical
 * @since 2024/4/30 21:33
 */
class CMIRegistrationHook : EnchantRegistrationHook {

    override fun getPluginName(): String = "CMI"

    override fun register() {
        CMIEnchantment.initialize()
        CMIEnchantment.saveEnchants()
    }

    companion object {

        @Awake(LifeCycle.LOAD)
        fun init() {
            EnchantRegistrationHooks.registerHook(CMIRegistrationHook())
        }
    }
}