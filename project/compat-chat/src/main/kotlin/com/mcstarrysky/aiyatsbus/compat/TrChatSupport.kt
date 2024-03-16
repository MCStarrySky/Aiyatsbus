package com.mcstarrysky.aiyatsbus.compat

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.util.trchatEnabled
import me.arasple.mc.trchat.module.internal.hook.HookPlugin
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.compat.TrChatSupport
 *
 * @author mical
 * @since 2024/3/16 17:26
 */
object TrChatSupport {

    @Awake(LifeCycle.ACTIVE)
    fun onActive() {
        if (trchatEnabled) {
            HookPlugin.registerDisplayItemHook("Aiyatsbus") { item, player ->
                Aiyatsbus.api().getDisplayManager().display(item, player)
            }
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun onDisable() {
        if (trchatEnabled) {
            HookPlugin.registry.removeIf { it.name == "Aiyatsbus" }
        }
    }
}