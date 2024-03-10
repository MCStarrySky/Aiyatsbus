package com.mcstarrysky.aiyatsbus.module.kether.action.bukkit.block

import com.mcstarrysky.aiyatsbus.module.kether.action.ActionProvider
import com.mcstarrysky.aiyatsbus.module.kether.action.bukkit.ActionLocation
import org.bukkit.block.Block
import taboolib.common.Inject
import taboolib.common.OpenResult
import taboolib.module.kether.KetherProperty
import taboolib.module.kether.ScriptProperty

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.bukkit.block.ActionBlock
 *
 * @author mical
 * @since 2024/3/10 11:07
 */
@Inject
object ActionBlock : ActionProvider<Block>() {

    override fun read(instance: Block, key: String): Any {
        if (key.isEmpty()) return instance
        val keys = key.split(".")
        val command = keys.toList().drop(1).joinToString(".")
        return when (keys.first()) {
            "location" -> ActionLocation.read(instance.location, command)
            "x" -> OpenResult.successful(instance.x)
            "y" -> OpenResult.successful(instance.y)
            "z" -> OpenResult.successful(instance.z)
            else -> OpenResult.successful()
        }
    }

    override fun write(instance: Block, key: String, value: Any?): OpenResult {
        TODO("Not yet implemented")
    }

    @KetherProperty(bind = Block::class)
    fun propertyBlock() = object : ScriptProperty<Block>("block.operator") {

        override fun read(instance: Block, key: String): OpenResult {
            return ActionBlock.read(instance, key)
                .let { if (it !is OpenResult) OpenResult.successful(it) else it }
        }

        override fun write(instance: Block, key: String, value: Any?): OpenResult {
            return OpenResult.successful()
        }
    }
}