package com.mcstarrysky.aiyatsbus.core.customization

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import taboolib.module.configuration.Configuration

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.customization.CustomTrigger
 *
 * @author mical
 * @since 2024/2/19 20:02
 */
abstract class CustomTrigger(
    open val enchant: AiyatsbusEnchantment,
    open val config: Configuration,
    open val type: TriggerType
) {

    abstract fun initialize()
}