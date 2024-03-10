package com.mcstarrysky.aiyatsbus.module.kether.action.bukkit.event.block

import com.mcstarrysky.aiyatsbus.module.kether.action.ActionProvider
import com.mcstarrysky.aiyatsbus.module.kether.action.bukkit.block.ActionBlock
import org.bukkit.event.block.BlockBreakEvent
import taboolib.common.Inject
import taboolib.common.OpenResult
import taboolib.module.kether.KetherProperty
import taboolib.module.kether.ScriptProperty

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.bukkit.event.block.ActionBlockBreakEvent
 *
 * @author mical
 * @since 2024/3/10 10:49
 */
@Inject
object ActionBlockBreakEvent : ActionProvider<BlockBreakEvent>() {

    override fun read(instance: BlockBreakEvent, key: String): Any? {
        if (key.isEmpty()) return instance
        val keys = key.split(".")
        val command = keys.toList().drop(1).joinToString(".")
        return when (keys.first()) {
            "player" -> instance.player
            else -> OpenResult.failed() // 前往父类寻找
        }
    }

    override fun write(instance: BlockBreakEvent, key: String, value: Any?): OpenResult {
        TODO("Not yet implemented")
    }

    @KetherProperty(bind = BlockBreakEvent::class)
    fun propertyBlockBreakEvent() = object : ScriptProperty<BlockBreakEvent>("blockBreakEvent.operator") {

        override fun read(instance: BlockBreakEvent, key: String): OpenResult {
            return ActionBlockBreakEvent.read(instance, key)
                .let { if (it !is OpenResult) OpenResult.successful(it) else it }
        }

        override fun write(instance: BlockBreakEvent, key: String, value: Any?): OpenResult {
            return OpenResult.successful()
        }
    }
}