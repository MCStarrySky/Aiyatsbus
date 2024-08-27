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