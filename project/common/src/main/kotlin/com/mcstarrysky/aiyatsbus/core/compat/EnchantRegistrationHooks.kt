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