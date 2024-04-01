package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.player.PlayerItemHeldEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player.PropertyPlayerItemHeldEvent
 *
 * @author yanshiqwq
 * @since 2024/4/2 00:51
 */
@AiyatsbusProperty(
    id = "player-item-held-event",
    bind = PlayerItemHeldEvent::class
)
class PropertyPlayerItemHeldEvent : AiyatsbusGenericProperty<PlayerItemHeldEvent>("player-item-held-event") {
    override fun readProperty(instance: PlayerItemHeldEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "previousSlot", "previous-slot", "prev-slot" -> instance.previousSlot
            "newSlot", "new-slot" -> instance.newSlot
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: PlayerItemHeldEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}