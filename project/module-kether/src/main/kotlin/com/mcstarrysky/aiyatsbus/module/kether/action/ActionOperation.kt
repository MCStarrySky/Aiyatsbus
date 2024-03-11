package com.mcstarrysky.aiyatsbus.module.kether.action

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusParser
import com.mcstarrysky.aiyatsbus.module.kether.action.operation.FastMultiBreak
import com.mcstarrysky.aiyatsbus.module.kether.action.operation.Plant
import com.mcstarrysky.aiyatsbus.module.kether.aiyatsbus
import org.bukkit.Location
import taboolib.module.kether.player

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionOperation
 *
 * @author mical
 * @since 2024/3/10 15:33
 */
object ActionOperation {

    @Suppress("UNCHECKED_CAST")
    @AiyatsbusParser(["operation"])
    fun operation() = aiyatsbus {
        when (nextToken()) {
            "fast-multi-break", "fastMultiBreak" -> {
                combine(any(), int()) { breaks, speed ->
                    FastMultiBreak.fastMultiBreak(
                        player().castSafely()!!,
                        (breaks as List<Location>).toMutableList(),
                        speed
                    )
                }
            }
            "plant" -> {
                combine(int(), text()) { side, seeds ->
                    Plant.plant(player().castSafely()!!, side, seeds)
                }
            }
            else -> error("unknown operation")
        }
    }
}