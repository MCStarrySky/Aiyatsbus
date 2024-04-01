package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player

import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.player.PlayerItemDamageEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player.PropertyPlayerItemDamageEvent
 *
 * @author yanshiqwq
 * @since 2024/4/2 00:34
 */
@AiyatsbusProperty(
    id = "player-item-damage-event",
    bind = PlayerItemDamageEvent::class
)
class PropertyPlayerItemDamageEvent : AiyatsbusGenericProperty<PlayerItemDamageEvent>("player-item-damage-event"){
    override fun readProperty(instance: PlayerItemDamageEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "item" -> instance.item
            "damage" -> instance.damage
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: PlayerItemDamageEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "damage" -> instance.damage = value?.coerceInt() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}