package com.mcstarrysky.aiyatsbus.module.kether.property.paper.destroystokyo.event.player

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.paper.event.player.PlayerElytraBoostEvent
 *
 * @author mical
 * @since 2024/7/17 14:24
 */
@AiyatsbusProperty(
    id = "player-elytra-boost-event",
    bind = PlayerElytraBoostEvent::class
)
class PropertyPlayerElytraBoostEvent : AiyatsbusGenericProperty<PlayerElytraBoostEvent>("player-elytra-boost-event") {

    override fun readProperty(instance: PlayerElytraBoostEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "itemStack", "item-stack", "item" -> instance.itemStack
            "firework" -> instance.firework
            "shouldConsume", "should-consume", "consume" -> instance.shouldConsume()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: PlayerElytraBoostEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "shouldConsume", "should-consume", "consume" -> instance.setShouldConsume(value?.coerceBoolean() ?: return OpenResult.successful())
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}