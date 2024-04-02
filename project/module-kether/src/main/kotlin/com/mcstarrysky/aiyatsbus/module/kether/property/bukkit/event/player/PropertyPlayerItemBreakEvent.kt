package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.player.PlayerItemBreakEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player.PropertyPlayerItemBreakEvent
 *
 * @author yanshiqwq
 * @since 2024/4/2 00:34
 */
@AiyatsbusProperty(
    id = "player-item-break-event",
    bind = PlayerItemBreakEvent::class
)
class PropertyPlayerItemBreakEvent : AiyatsbusGenericProperty<PlayerItemBreakEvent>("player-item-break-event") {

    override fun readProperty(instance: PlayerItemBreakEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "brokenItem", "item" -> instance.brokenItem
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: PlayerItemBreakEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}