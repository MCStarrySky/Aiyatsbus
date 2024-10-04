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
package com.mcstarrysky.aiyatsbus.module.kether.action.game

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusParser
import com.mcstarrysky.aiyatsbus.module.kether.aiyatsbus

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.game.ActionVariables
 *
 * @author mical
 * @since 2024/3/10 16:03
 */
@Deprecated("等待重写, 现在的太智障了")
object ActionVariables {

    // TODO: getBoolean, getDouble, getStringList, etc.

    /**
     * ordinary-var &enchant name
     */
    @AiyatsbusParser(["ordinary-var"])
    fun ordinaryVar() = aiyatsbus {
        combine(any(), text()) { ench, name -> (ench as AiyatsbusEnchantment).variables.ordinary(name) }
    }

    /**
     * level-var &enchant name $level true
     */
    @AiyatsbusParser(["level-var"])
    fun levelVar() = aiyatsbus {
        combine(any(), text(), int(), bool()) { ench, name, level, withUnit -> (ench as AiyatsbusEnchantment).variables.leveled(name, level, withUnit) }
    }

    /**
     * modifiable-var &enchant $item name
     */
    @AiyatsbusParser(["modifiable-var"])
    fun modifiableVar() = aiyatsbus {
        combine(any(), item(), text()) { ench, item, name -> (ench as AiyatsbusEnchantment).variables.modifiable(name, item) }
    }

    /**
     * modifiable-var-set &enchant $item name [to] $value
     */
    @AiyatsbusParser(["modifiable-var-set"])
    fun modifiableVarSet() = aiyatsbus {
        combine(any(), item(), text(), trim("to", "=", then = any())) { ench, item, name, value -> (ench as AiyatsbusEnchantment).variables.modifyVariable(item, name, value.toString()) }
    }
}