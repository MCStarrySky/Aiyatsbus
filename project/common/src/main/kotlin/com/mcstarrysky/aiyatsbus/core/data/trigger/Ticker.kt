package com.mcstarrysky.aiyatsbus.core.data.trigger

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import taboolib.common.platform.function.warning
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.kether.LocalizedException

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.trigger.Ticker
 *
 * @author mical
 * @since 2024/3/20 22:28
 */
data class Ticker @JvmOverloads constructor(
    private val root: ConfigurationSection,
    private val enchant: AiyatsbusEnchantment,
    val preHandle: String = root.getString("pre-handle") ?: "",
    val handle: String = root.getString("handle") ?: "",
    val postHandle: String = root.getString("post-handle") ?: "",
    val interval: Long = root.getLong("interval", 20L)
) {

    init {
        if (AiyatsbusSettings.enableKetherPreheat) {
            try {
                Aiyatsbus.api().getKetherHandler().preheat(preHandle)
                Aiyatsbus.api().getKetherHandler().preheat(handle)
                Aiyatsbus.api().getKetherHandler().preheat(postHandle)
            } catch (ex: LocalizedException) {
                warning("Unable to preheat the ticker ${root.name} of enchantment ${enchant.id}: $ex")
            }
        }
    }
}