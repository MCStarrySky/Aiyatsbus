package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player

import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.player.PlayerItemMendEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player.PropertyPlayerItemMendEvent
 *
 * @author yanshiqwq
 * @since 2024/4/2 00:55
 */
@AiyatsbusProperty(
    id = "player-item-mend-event",
    bind = PlayerItemMendEvent::class
)
class PropertyPlayerItemMendEvent : AiyatsbusGenericProperty<PlayerItemMendEvent>("player-item-mend-event") {
    override fun readProperty(instance: PlayerItemMendEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "item" -> instance.item
            "slot" -> instance.slot
            "experienceOrb", "experience-orb", "exp-orb", "exp" -> instance.experienceOrb
            "repairAmount", "repair-amount" -> instance.repairAmount
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: PlayerItemMendEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "damage" -> instance.repairAmount = value?.coerceInt() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}