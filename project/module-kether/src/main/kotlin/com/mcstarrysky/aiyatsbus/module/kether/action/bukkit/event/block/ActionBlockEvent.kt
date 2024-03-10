package com.mcstarrysky.aiyatsbus.module.kether.action.bukkit.event.block

import com.mcstarrysky.aiyatsbus.module.kether.action.ActionProvider
import com.mcstarrysky.aiyatsbus.module.kether.action.bukkit.block.ActionBlock
import com.mcstarrysky.aiyatsbus.module.kether.action.bukkit.event.ActionEvent
import org.bukkit.event.block.BlockEvent
import taboolib.common.Inject
import taboolib.common.OpenResult
import taboolib.module.kether.KetherProperty
import taboolib.module.kether.ScriptProperty

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.bukkit.event.block.ActionBlockEvent
 *
 * @author mical
 * @since 2024/3/10 10:46
 */
@Inject
object ActionBlockEvent : ActionProvider<BlockEvent>() {

    override fun read(instance: BlockEvent, key: String): Any {
        if (key.isEmpty()) return instance
        val keys = key.split(".")
        val command = keys.toList().drop(1).joinToString(".")
        return when (keys.first()) {
            "block" -> {
                ActionBlock.read(instance.block, command)
            }
            else -> OpenResult.failed() // 前往它的父类查找
        }
    }

    override fun write(instance: BlockEvent, key: String, value: Any?): OpenResult {
        return ActionEvent.write(instance, key, value) // BlockEvent 这个抽象事件没有自己的属性啊，所以直接前往它的父类
    }

    @KetherProperty(bind = BlockEvent::class)
    fun propertyBlockEvent() = object : ScriptProperty<BlockEvent>("blockEvent.operator") {

        override fun read(instance: BlockEvent, key: String): OpenResult {
            return ActionBlockEvent.read(instance, key)
                .let { if (it !is OpenResult) OpenResult.successful(it) else it }
        }

        override fun write(instance: BlockEvent, key: String, value: Any?): OpenResult {
            return OpenResult.failed()
        }
    }
}