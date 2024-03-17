package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.player.PlayerToggleSneakEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player.PropertyPlayerToggleSneakEvent
 *
 * @author mical
 * @since 2024/3/17 17:46
 */
@AiyatsbusProperty(
    id = "player-toggle-sneak",
    bind = PlayerToggleSneakEvent::class
)
class PropertyPlayerToggleSneakEvent : AiyatsbusGenericProperty<PlayerToggleSneakEvent>("player-toggle-sneak") {

    override fun readProperty(instance: PlayerToggleSneakEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "isSneaking", "is-sneaking", "sneaking", "sneak" -> instance.isSneaking
            else -> OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: PlayerToggleSneakEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}