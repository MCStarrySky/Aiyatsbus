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
package com.mcstarrysky.aiyatsbus.impl.enchant

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentBase
import com.mcstarrysky.aiyatsbus.core.data.trigger.Trigger
import taboolib.module.configuration.Configuration
import java.io.File

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.enchant.SplendidEnchant
 *
 * @author mical
 * @date 2024/8/21 17:43
 */
class InternalAiyatsbusEnchantment(
    id: String,
    file: File,
    config: Configuration
) : AiyatsbusEnchantmentBase(id, file, config) {

    /**
     * 附魔的触发器
     */
    override val trigger: Trigger = Trigger(config.getConfigurationSection("mechanisms"), this)
}