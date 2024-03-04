package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.event

import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.*
import org.bukkit.entity.LivingEntity
import org.bukkit.event.block.BlockBreakEvent

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.event.EventBreak
 *
 * @author mical
 * @since 2024/3/4 21:03
 */
object EventBreak : EventEntry<BlockBreakEvent>() {

    override fun modify(event: BlockBreakEvent, entity: LivingEntity, cmd: String, params: List<String>): Boolean {
        when (cmd) {
            "取消破坏" -> event.isCancelled = true
            "设置掉落经验" -> event.expToDrop = params.firstOrNull()?.toIntOrNull() ?: 0
            "设置是否掉落物品" -> event.isDropItems = params.firstOrNull()?.toBoolean() ?: false
            else -> {}
        }
        return true
    }

    override fun get(event: BlockBreakEvent, objName: String): Pair<ObjectEntry<*>, Any?> {
        return when (objName) {
            "掉落经验" -> objString.h(event.expToDrop)
            "是否掉落物品" -> objString.h(event.isDropItems)
            "破坏者" -> objPlayer.h(event.player)
            "方块" -> objBlock.holderize(event.block)
            else -> objString.h(null)
        }
    }
}