package com.mcstarrysky.aiyatsbus.module.bukkit

import com.mcstarrysky.aiyatsbus.impl.DefaultAiyatsbusBooster
import taboolib.common.LifeCycle
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.disablePlugin
import taboolib.common.platform.function.registerLifeCycleTask

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.bukkit.AiyatsbusPlugin
 *
 * @author mical
 * @since 2024/2/17 15:25
 */
object AiyatsbusPlugin : Plugin() {

    init {
        registerLifeCycleTask(LifeCycle.INIT) {
            try {
                DefaultAiyatsbusBooster.startup()
            } catch (ex: Throwable) {
                ex.printStackTrace()
                disablePlugin()
            }
        }
    }
}