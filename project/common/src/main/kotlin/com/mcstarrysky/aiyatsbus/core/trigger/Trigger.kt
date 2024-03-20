package com.mcstarrysky.aiyatsbus.core.trigger

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import taboolib.library.configuration.ConfigurationSection
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.trigger.Trigger
 *
 * @author mical
 * @since 2024/3/9 18:36
 */
data class Trigger(private val section: ConfigurationSection?, private val enchant: AiyatsbusEnchantment) {

    val listeners: ConcurrentHashMap<String, EventExecutor> = ConcurrentHashMap()
    val tickers: ConcurrentHashMap<String, Ticker> = ConcurrentHashMap()

    init {
        loadListeners()
    }

    private fun loadListeners() {
        listeners.clear()

        section?.getConfigurationSection("listeners")?.let { listenersSection ->
            listeners += listenersSection.getKeys(false)
                .associateWith { EventExecutor.load(listenersSection.getConfigurationSection(it)!!) }
        }
        section?.getConfigurationSection("tickers")?.let { tickersSection ->
            tickers += tickersSection.getKeys(false)
                .associateWith { Ticker.load(tickersSection.getConfigurationSection(it)!!) }
                .mapKeys { "${enchant.basicData.id}.$it" }
                .also {
                    it.entries.forEach { (id, ticker) -> Aiyatsbus.api().getTickHandler().getRoutine().put(enchant, id, ticker.interval) }
                }
        }
    }

    fun onDisable() {
        listeners.clear()
    }
}