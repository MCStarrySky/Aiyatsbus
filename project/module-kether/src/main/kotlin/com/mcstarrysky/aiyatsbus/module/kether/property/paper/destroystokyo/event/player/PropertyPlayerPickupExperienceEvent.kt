package com.mcstarrysky.aiyatsbus.module.kether.property.paper.destroystokyo.event.player

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.paper.destroystokyo.event.player.PropertyPlayerPickupExperienceEvent
 *
 * @author mical
 * @since 2024/8/19 08:41
 */
@AiyatsbusProperty(
    id = "player-pickup-experience-event",
    bind = PlayerPickupExperienceEvent::class
)
class PropertyPlayerPickupExperienceEvent : AiyatsbusGenericProperty<PlayerPickupExperienceEvent>("player-pickup-experience-event") {

    override fun readProperty(instance: PlayerPickupExperienceEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "experienceOrb", "experience-orb" -> instance.experienceOrb
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: PlayerPickupExperienceEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}