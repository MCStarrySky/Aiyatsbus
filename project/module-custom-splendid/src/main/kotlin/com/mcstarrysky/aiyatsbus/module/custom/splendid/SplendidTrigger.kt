package com.mcstarrysky.aiyatsbus.module.custom.splendid

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.customization.CustomTrigger
import com.mcstarrysky.aiyatsbus.core.customization.TriggerType
import com.mcstarrysky.aiyatsbus.core.data.VariableType
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.Listeners
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.Tickers
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.*
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.util.asMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.custom.splendid.SplendidTrigger
 *
 * @author mical
 * @since 2024/2/20 00:15
 */
class SplendidTrigger(
    override val enchant: AiyatsbusEnchantment,
    override val config: Configuration, // 附魔的整个配置文件
    override val type: TriggerType
) : CustomTrigger(enchant, config, type) {

    // 变量名 - 类型 to 初始值
    val flexible = mutableMapOf<String, Pair<ObjectEntry<*>, String>>()

    lateinit var listeners: Listeners

    lateinit var tickers: Tickers

    override fun initialize() {
        config.getConfigurationSection("variables")?.getConfigurationSection("flexible").asMap().forEach { (variable, expression) ->
            val type = when (expression.toString().split("::")[0]) {
                "block" -> objBlock
                "entity" -> objEntity
                "living_entity" -> objLivingEntity
                "player" -> objPlayer
                "item" -> objItem
                "vector" -> objVector
                "location" -> objLocation
                else -> objString
            }
            val init = expression.toString().split("::")[1]
            flexible[variable] = type to init
            enchant.variables.variables[variable] = VariableType.FLEXIBLE
        }

        listeners = Listeners(enchant, this, config.getConfigurationSection("mechanisms.listeners"))
        tickers = Tickers(enchant, config.getConfigurationSection("mechanisms.tickers"))
    }
}