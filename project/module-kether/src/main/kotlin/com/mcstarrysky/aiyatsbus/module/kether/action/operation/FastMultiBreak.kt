package com.mcstarrysky.aiyatsbus.module.kether.action.operation

import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import com.mcstarrysky.aiyatsbus.core.util.PermissionChecker
import com.mcstarrysky.aiyatsbus.core.util.itemsAdderEnabled
import com.mcstarrysky.aiyatsbus.core.util.serialized
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusParser
import com.mcstarrysky.aiyatsbus.module.kether.aiyatsbus
import dev.lone.itemsadder.api.CustomBlock
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
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

    /**
     * 一定要存这个, 在破坏额外方块的时候删除, 避免递归永远挖下去
     */
    private val extraBlocks = mutableListOf<Location>()

    private fun breakExtra(block: Block) {
        extraBlocks += block.location
    }

    @AiyatsbusParser(["extraBlocks", "extra-blocks"])
    fun extraBlocks() = aiyatsbus {
        discrete { extraBlocks }
    }

    private fun breakExtraBlock(player: Player, block: Block) {
        try {
            breakExtra(block)
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
            extraBlocks -= block.location
        }
    }

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

                if (!PermissionChecker.hasBlockPermission(player, block)) continue

                breakExtraBlock(player, block)
            }
        }
    }
}