package com.mcstarrysky.aiyatsbus.core.trigger

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import com.mcstarrysky.aiyatsbus.core.util.baffle
import com.mcstarrysky.aiyatsbus.core.util.coerceLong
import taboolib.common5.Baffle
import taboolib.library.configuration.ConfigurationSection

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.trigger.Ticker
 *
 * @author mical
 * @since 2024/3/20 22:28
 */
data class Ticker(
    val preHandle: String,
    val handle: String,
    val postHandle: String,
    val interval: Long
) {

    @Transient
    var baffle: Baffle? = null

    private fun preheat() {
        if (AiyatsbusSettings.enableKetherPreheat) {
            Aiyatsbus.api().getKetherHandler().preheat(preHandle)
            Aiyatsbus.api().getKetherHandler().preheat(handle)
            Aiyatsbus.api().getKetherHandler().preheat(postHandle)
        }
    }

    companion object {

        fun load(tickerSection: ConfigurationSection): Ticker {
            val ticker = Ticker(
                tickerSection["pre-handle"]?.toString() ?: "",
                tickerSection["handle"]?.toString() ?: "",
                tickerSection["post-handle"]?.toString() ?: "",
                tickerSection["interval"].coerceLong(20L)
            ).apply {
                baffle = tickerSection.getConfigurationSection("baffle").baffle()
            }
            ticker.preheat()
            return ticker
        }
    }
}