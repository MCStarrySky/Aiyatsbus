package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.`object`

import com.mcstarrysky.aiyatsbus.core.util.*
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.*
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.Ageable
import taboolib.library.reflex.Reflex.Companion.invokeMethod

object ObjectBlock : ObjectEntry<Block>() {

    val crops = mapOf(
        Material.WHEAT to Material.WHEAT,
        Material.PUMPKIN to Material.PUMPKIN,
        Material.MELON to Material.MELON,
        Material.BEETROOTS to Material.BEETROOT,
        Material.CARROTS to Material.CARROT,
        Material.POTATOES to Material.POTATO,
        Material.SWEET_BERRIES to Material.SWEET_BERRIES,
        Material.GLOW_BERRIES to Material.GLOW_BERRIES
    )

    override fun modify(
        obj: Block,
        cmd: String,
        params: List<String>
    ): Boolean {
        when (cmd) {
            "破坏" -> FurtherOperation.furtherBreak(objPlayer.disholderize(params[0]), obj)
            "放置" -> FurtherOperation.furtherPlace(objPlayer.disholderize(params[0]), obj, Material.valueOf(params[1]))
            "设置年龄" -> (obj.blockData as? Ageable)?.let {
                it.age = params[0].calcToInt().coerceAtLeast(0).coerceAtMost(it.maximumAge)
                obj.blockData = it
            }

            "临时方块" -> {
                val player = objPlayer.disholderize(params[0])
                val material = Material.valueOf(params[1])
                val duration = params[2, "0.5"].calcToDouble()
                player.tmpBlock(obj.location, material, duration)
            }
        }
        return true
    }

    override fun get(from: Block, objName: String): Pair<ObjectEntry<*>, Any?> {
        if (objName.startsWith("临近方块")) { //格式 临近方块(x,y,z)
            val numbers = objName.numbers
            val x = numbers[0]
            val y = numbers[1]
            val z = numbers[2]
            val loc = from.location.clone().also { it.add(x, y, z) }
            return objBlock.holderize(loc.block)
        }
        return when (objName) {
            "类型" -> objString.h(from.type)
            "生物群系" -> objString.h(from.biome)
            "充能等级" -> objString.h(from.blockPower)
            "位置" -> objLocation.h(from.location)
            "x" -> objString.h(from.x)
            "y" -> objString.h(from.y)
            "z" -> objString.h(from.z)
            "是否为农作物" -> objString.h(crops.containsKey(from.type))
            "年龄" -> objString.h((from.blockData as? Ageable)?.age ?: 0)
            else -> runCatching {
                objString.h(from.invokeMethod(objName)) // 可以直接填写 "isOnGround" 此类
            }.getOrElse { objString.h(null) }
        }
    }

    override fun holderize(obj: Block) = this to "方块=${obj.location.serialized}"

    override fun disholderize(holder: String) = holder.replace("方块=", "").toLoc().block
}