package com.mcstarrysky.aiyatsbus.core.event

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import org.bukkit.block.Block
import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent

/**
 * 用以处理额外方块挖掘, 例如立方挖掘的 3*3*3 范围方块
 * 不过不包含原始方块, 比如立方对着挖下去的那个方块
 *
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.event.ExtraBlockBreakEvent
 *
 * @author mical
 * @since 2024/3/7 17:49
 */
class AiyatsbusBlockBreakEvent(val player: Player, val block: Block, val enchant: AiyatsbusEnchantment?, val level: Int?) : BukkitProxyEvent()