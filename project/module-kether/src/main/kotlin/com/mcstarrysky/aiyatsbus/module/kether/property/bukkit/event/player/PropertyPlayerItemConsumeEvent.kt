package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.player.PlayerItemConsumeEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player.PropertyPlayerItemConsumeEvent
 *
 * @author yanshiqwq
 * @since 2024/4/2 00:44
 */
@AiyatsbusProperty(
    id = "player-item-consume-event",
    bind = PlayerItemConsumeEvent::class
)
class PropertyPlayerItemConsumeEvent : AiyatsbusGenericProperty<PlayerItemConsumeEvent>("player-item-consume-event") {
    override fun readProperty(instance: PlayerItemConsumeEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "item" -> instance.item
            "hand" -> instance.hand
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: PlayerItemConsumeEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}