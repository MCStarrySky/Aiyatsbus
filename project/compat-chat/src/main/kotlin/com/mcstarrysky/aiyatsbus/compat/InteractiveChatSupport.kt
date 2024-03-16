package com.mcstarrysky.aiyatsbus.compat

import com.loohp.interactivechat.api.events.ItemPlaceholderEvent
import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.util.interactiveChatEnabled
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.unregisterListener

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.compat.InteractiveChatSupport
 *
 * @author mical
 * @since 2024/3/16 17:30
 */
object InteractiveChatSupport {

    private val hook: ProxyListener? = null

    @Awake(LifeCycle.ACTIVE)
    fun onActive() {
        if (interactiveChatEnabled) {
            registerBukkitListener(ItemPlaceholderEvent::class.java) { e ->
                e.itemStack = Aiyatsbus.api().getDisplayManager().display(e.itemStack, e.receiver)
            }
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun onDisable() {
        if (interactiveChatEnabled) {
            unregisterListener(hook ?: return)
        }
    }
}