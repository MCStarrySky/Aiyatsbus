package com.mcstarrysky.aiyatsbus.core.data.registry

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import com.mcstarrysky.aiyatsbus.core.StandardPriorities
import com.mcstarrysky.aiyatsbus.core.sendLang
import com.mcstarrysky.aiyatsbus.core.util.inject.Reloadable
import com.mcstarrysky.aiyatsbus.core.util.inject.AwakePriority
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.common.platform.function.warning
import taboolib.library.kether.LocalizedException
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.data.registry.InternalTriggers
 *
 * @author mical
 * @since 2024/8/19 08:03
 */
data class InternalTrigger(
    val id: String,
    val script: String
) {

    init {
        if (AiyatsbusSettings.enableKetherPreheat) {
            try {
                Aiyatsbus.api().getKetherHandler().preheat(script)
            } catch (ex: LocalizedException) {
                warning("Unable to preheat the internal trigger $id: $ex")
            }
        }
    }
}

object InternalTriggerLoader {

    @Config("enchants/internal-triggers.yml", autoReload = true)
    lateinit var config: Configuration
        private set

    val registered: ConcurrentHashMap<String, InternalTrigger> = ConcurrentHashMap()

    private var isLoaded = false

    @Reloadable
    @AwakePriority(LifeCycle.ENABLE, StandardPriorities.INTERNAL_TRIGGERS)
    fun init() {
        if (isLoaded) {
            config.reload()
            return
        }
        load()
        isLoaded = true
    }

    @Awake(LifeCycle.ENABLE)
    fun reload() {
        registerLifeCycleTask(LifeCycle.ENABLE) {
            config.onReload {
                load()
            }
        }
    }

    private fun load() {
        val time = System.currentTimeMillis()
        registered.clear()
        registered += config.getKeys(false).map { it to InternalTrigger(it, config[it].toString()) }
        console().sendLang("loading-internal-triggers", registered.size, System.currentTimeMillis() - time)
    }
}