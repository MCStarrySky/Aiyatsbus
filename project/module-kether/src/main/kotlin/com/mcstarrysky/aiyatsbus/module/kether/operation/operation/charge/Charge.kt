package com.mcstarrysky.aiyatsbus.module.kether.operation.operation.charge

import com.mcstarrysky.aiyatsbus.core.event.AiyatsbusBowChargeEvent
import com.mcstarrysky.aiyatsbus.core.util.coerceDouble
import com.mcstarrysky.aiyatsbus.core.util.realDamage
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
import taboolib.library.xseries.XMaterial
import taboolib.platform.util.checkItem

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.operation.operation.charge.Charge
 *
 * @author mical
 * @since 2024/9/19 22:07
 */
object Charge {

    private val arrows = listOfNotNull(
        XMaterial.ARROW.parseItem(),
        XMaterial.SPECTRAL_ARROW.parseItem(),
        XMaterial.TIPPED_ARROW.parseItem()
    )

    @Suppress("UNCHECKED_CAST")
    fun charge(args: List<Any?>?) {
        args ?: return
        charge(
            args[0] as? Player ?: return,
            args[1].coerceDouble(),
            args[2] as? AiyatsbusBowChargeEvent.Prepare ?: return
        )
    }

    fun charge(player: Player, health: Double, event: AiyatsbusBowChargeEvent.Prepare) {
        if (arrows.any { player.inventory.checkItem(it) }) {
            return
        }
        if (player.health >= health) {
            submit {
                player.realDamage(health)
            }
            event.isAllowed = true
        }
    }
}