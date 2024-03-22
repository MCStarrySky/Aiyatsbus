package com.mcstarrysky.aiyatsbus.module.kether.action.operation

import com.mcstarrysky.aiyatsbus.core.AntiGriefChecker
import com.mcstarrysky.aiyatsbus.core.FurtherOperation
import com.mcstarrysky.aiyatsbus.core.util.serialized
import org.bukkit.Location
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.warning

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.operation.FastMultiBreak
 *
 * @author mical
 * @since 2024/3/10 15:34
 */
object FastMultiBreak {

    fun fastMultiBreak(player: Player, breaks: MutableList<Location>, speed: Int) {
        submit(delay = 0, period = 1) {
            for (i in 0 until speed) {

                val loc = breaks.firstOrNull()
                if (loc == null) {
                    cancel()
                    continue
                }

                val block = loc.block
                breaks -= loc

                if (block.location.world != player.world) {
                    warning("检测到玩家 ${player.name} 使用附魔破坏了一个不在同一世界的方块，可能是作弊行为或是极端情况下的极小概率行为，已忽略该破坏")
                    warning("|- 方块信息: ${block.location.serialized}")
                    continue
                }

                if (!AntiGriefChecker.canBreak(player, block)) continue
                FurtherOperation.furtherBreak(player, block)
            }
        }
    }
}