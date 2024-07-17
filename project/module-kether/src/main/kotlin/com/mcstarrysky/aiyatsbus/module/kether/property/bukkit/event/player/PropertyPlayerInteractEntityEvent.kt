package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.player.PlayerInteractEntityEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player.PropertyPlayerInteractEntityEvent
 *
 * @author yanshiqwq
 * @since 2024/4/4 10:33
 */
@AiyatsbusProperty(
    id = "player-interact-entity-event",
    bind = PlayerInteractEntityEvent::class
)
class PropertyPlayerInteractEntityEvent : AiyatsbusGenericProperty<PlayerInteractEntityEvent>("player-interact-entity-event") {

    override fun readProperty(instance: PlayerInteractEntityEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "rightClicked", "right-clicked", "entity" -> instance.rightClicked
            "hand" -> instance.hand
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: PlayerInteractEntityEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}