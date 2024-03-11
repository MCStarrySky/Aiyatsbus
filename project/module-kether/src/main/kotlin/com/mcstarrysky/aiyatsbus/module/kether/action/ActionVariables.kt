package com.mcstarrysky.aiyatsbus.module.kether.action

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusParser
import com.mcstarrysky.aiyatsbus.module.kether.aiyatsbus

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionVariables
 *
 * @author mical
 * @since 2024/3/10 16:03
 */
object ActionVariables {

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
     * player-related-var &enchant name &player
     */
    @AiyatsbusParser(["player-related-var"])
    fun playerRelatedVar() = aiyatsbus {
        combine(any(), text(), player()) { ench, name, player -> (ench as AiyatsbusEnchantment).variables.playerRelated(name, player) }
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
        combine(any(), item(), text(), optional("to", "=", then = any())) { ench, item, name, value -> (ench as AiyatsbusEnchantment).variables.modifyVariable(item, name, value.toString()) }
    }
}