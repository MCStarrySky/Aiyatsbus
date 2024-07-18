package com.mcstarrysky.aiyatsbus.core.compat

import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.platform.util.bukkitPlugin
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.compat.EnchantRegistrationHooks
 *
 * @author mical
 * @since 2024/4/30 21:22
 */
interface EnchantRegistrationHook {

    fun getPluginName(): String

    fun register() {
    }

    fun unregister() {
    }
}

object EnchantRegistrationHooks {

    val registered: ConcurrentHashMap<String, EnchantRegistrationHook> = ConcurrentHashMap()

    fun registerHooks() {
        registered.values.forEach { registerHook(it) }
    }

    fun registerHook(hook: EnchantRegistrationHook) {
        if (bukkitPlugin.server.pluginManager.getPlugin(hook.getPluginName()) != null) {
            kotlin.runCatching { hook.register() }
        }
    }

    fun unregisterHook(hook: EnchantRegistrationHook) {
        if (bukkitPlugin.server.pluginManager.getPlugin(hook.getPluginName()) != null) {
            kotlin.runCatching { hook.unregister() }
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun unregisterHooks() {
        registered.values.forEach { unregisterHook(it) }
    }
}