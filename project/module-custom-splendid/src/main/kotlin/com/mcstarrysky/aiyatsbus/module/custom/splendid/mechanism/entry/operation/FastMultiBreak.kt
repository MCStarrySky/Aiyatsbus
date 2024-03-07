package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.operation

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import com.mcstarrysky.aiyatsbus.core.event.AiyatsbusBlockBreakEvent
import com.mcstarrysky.aiyatsbus.core.util.PermissionChecker
import com.mcstarrysky.aiyatsbus.core.util.itemsAdderEnabled
import com.mcstarrysky.aiyatsbus.core.util.serialized
import com.mcstarrysky.aiyatsbus.module.custom.splendid.listener.caller.BlockBreak
import dev.lone.itemsadder.api.CustomBlock
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.warning

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.operation.FastMultiBreak
 *
 * @author mical
 * @since 2024/3/7 17:25
 */
object FastMultiBreak {

    fun breakExtraBlock(player: Player, block: Block, ench: AiyatsbusEnchantment? = null, level: Int? = null) {
        if (AiyatsbusBlockBreakEvent(player, block, ench, level).call()) {
            println(1)
            try {
                BlockBreak.breakExtra(block)
                player.breakBlock(block)
            } catch (ex: Throwable) {
                ex.printStackTrace()
            } finally {
                if (block.type != Material.AIR) {
                    if (AiyatsbusSettings.supportItemsAdder && itemsAdderEnabled) {
                        if (CustomBlock.byAlreadyPlaced(block) != null) {
                            CustomBlock.getLoot(block, player.inventory.itemInMainHand, true).forEach {
                                player.world.dropItem(block.location, it)
                            }
                            CustomBlock.remove(block.location)
                        } else {
                            block.breakNaturally(player.inventory.itemInMainHand)
                        }
                    } else {
                        block.breakNaturally(player.inventory.itemInMainHand)
                    }
                }
                BlockBreak.extraBlocks -= block.location.serialized
            }
        }
    }

    fun fastMultiBreak(player: Player, breaks: MutableList<Location>, speed: Int, ench: AiyatsbusEnchantment? = null, level: Int? = null) {
        submit(delay = 0L, period = 0L) {
            for (i in 0 until speed) {
                val loc = breaks.firstOrNull()
                println(loc)
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

                if (!PermissionChecker.hasBlockPermission(player, block)) continue

                println("break")

                breakExtraBlock(player, block, ench, level)
            }
        }
    }
}