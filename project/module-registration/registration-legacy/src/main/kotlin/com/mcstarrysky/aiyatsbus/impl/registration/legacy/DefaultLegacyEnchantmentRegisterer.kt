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
package com.mcstarrysky.aiyatsbus.impl.registration.legacy

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentBase
import com.mcstarrysky.aiyatsbus.core.registration.AiyatsbusEnchantmentRegisterer
import org.bukkit.enchantments.Enchantment
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.nms.MinecraftVersion

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.registration.legacy.DefaultLegacyEnchantmentRegisterer
 *
 * @author mical
 * @since 2024/2/17 18:51
 */
object DefaultLegacyEnchantmentRegisterer : AiyatsbusEnchantmentRegisterer {

    @Awake(LifeCycle.CONST)
    fun init() {
        if (MinecraftVersion.majorLegacy <= 12002) {
            Enchantment::class.java.setProperty("acceptingNew", value = true, isStatic = true)
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun exit() {
        if (MinecraftVersion.majorLegacy <= 12002) {
            Enchantment::class.java.setProperty("acceptingNew", value = false, isStatic = true)
        }
    }

    override fun register(enchant: AiyatsbusEnchantmentBase): Enchantment {
        val enchantment = LegacyCraftEnchantment(enchant)
        // 不需要向 Bukkit 注册原版附魔
        if (!enchant.alternativeData.isVanilla) {
            Enchantment.registerEnchantment(enchantment)
        }
        return enchantment
    }

    override fun unregister(enchant: AiyatsbusEnchantment) {
        // 肯定不能卸载原版附魔啊, 想什么呢?
        if (!enchant.alternativeData.isVanilla) {
            Enchantment::class.java.getProperty<HashMap<*, *>>("byKey", true)!!.remove(enchant.enchantmentKey)
            Enchantment::class.java.getProperty<HashMap<*, *>>("byName", true)!!.remove(enchant.id.uppercase())
        }
    }
}