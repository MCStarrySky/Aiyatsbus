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
package com.mcstarrysky.aiyatsbus.core.data.trigger

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.data.trigger.event.EventExecutor
import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import taboolib.library.configuration.ConfigurationSection
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.trigger.Trigger
 *
 * @author mical
 * @since 2024/3/9 18:36
 */
data class Trigger(
    private val section: ConfigurationSection?,
    private val enchant: AiyatsbusEnchantment,
    val tickerPriority: Int = (section?.getString("tickerPriority")
        ?: section?.getString("ticker-priority")).coerceInt(0),
    val listenerPriority: Int = (section?.getString("listenerPriority")
        ?: section?.getString("listener-priority")).coerceInt(0),
) {

    val listeners: ConcurrentHashMap<String, EventExecutor> = ConcurrentHashMap()
    val tickers: ConcurrentHashMap<String, Ticker> = ConcurrentHashMap()

    init {
        section?.getConfigurationSection("listeners")?.let { listenersSection ->
            listeners += listenersSection.getKeys(false)
                .associateWith { EventExecutor(listenersSection.getConfigurationSection(it)!!, enchant) }
        }
        section?.getConfigurationSection("tickers")?.let { tickersSection ->
            tickers += tickersSection.getKeys(false)
                .associateWith { Ticker(tickersSection.getConfigurationSection(it)!!, enchant) }
                .mapKeys { "${enchant.basicData.id}.$it" }.also {
                    it.entries.forEach { (id, ticker) ->
                        Aiyatsbus.api().getTickHandler().getRoutine().put(enchant, id, ticker.interval)
                    }
                }
        }
    }

    fun onDisable() {
        listeners.clear()
        tickers.keys.forEach { Aiyatsbus.api().getTickHandler().getRoutine().remove(enchant, it) }
        tickers.clear()
    }
}