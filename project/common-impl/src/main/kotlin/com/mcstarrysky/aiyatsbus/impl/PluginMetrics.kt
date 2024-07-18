package com.mcstarrysky.aiyatsbus.impl

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.function.pluginVersion
import taboolib.module.metrics.Metrics
import taboolib.module.metrics.charts.SingleLineChart

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.PluginMetrics
 *
 * @author mical
 * @since 2024/3/19 23:20
 */
object PluginMetrics {

    lateinit var metrics: Metrics
        private set

    @Awake(LifeCycle.ACTIVE)
    private fun init() {
        metrics = Metrics(21363, pluginVersion, Platform.BUKKIT)

        // enchantments
        metrics.addCustomChart(SingleLineChart("enchantments") {
            Aiyatsbus.api().getEnchantmentManager().getEnchants().size
        })

        // kether_triggers
        metrics.addCustomChart(SingleLineChart("kether_triggers") {
            Aiyatsbus.api().getEnchantmentManager().getEnchants().values.map { it.trigger.listeners.values }.flatten().size
        })
    }
}