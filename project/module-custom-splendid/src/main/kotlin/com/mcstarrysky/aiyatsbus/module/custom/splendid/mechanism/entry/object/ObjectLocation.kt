package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.`object`

import com.mcstarrysky.aiyatsbus.core.util.calcToDouble
import com.mcstarrysky.aiyatsbus.core.util.serialized
import com.mcstarrysky.aiyatsbus.core.util.toLoc
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.ObjectEntry
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objBlock
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objLocation
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objString
import org.bukkit.Location

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.`object`.ObjectLocation
 *
 * @author mical
 * @since 2024/3/4 22:55
 */
object ObjectLocation : ObjectEntry<Location>() {

    override fun modify(obj: Location, cmd: String, params: List<String>): Boolean {
        when (cmd) {
            // FIXME
            "加" -> obj.add(params[0].calcToDouble(), params[1].calcToDouble(), params[2].calcToDouble())
        }
        return true
    }

    override fun get(from: Location, objName: String): Pair<ObjectEntry<*>, Any?> {
        return when (objName) {
            "方块" -> objBlock.h(from.block)
            "克隆" -> objLocation.h(from.clone())
            "x" -> objString.h(from.x)
            "y" -> objString.h(from.y)
            "z" -> objString.h(from.z)
            "x取整" -> objString.h(from.blockX)
            "y取整" -> objString.h(from.blockY)
            "z取整" -> objString.h(from.blockZ)
            "pitch" -> objString.h(from.pitch)
            "yaw" -> objString.h(from.yaw)
            else -> objString.h("?")
        }
    }

    override fun holderize(obj: Location): Pair<ObjectEntry<Location>, String> {
        return this to "位置=${obj.serialized}"
    }

    override fun disholderize(holder: String): Location {
        return holder.removePrefix("位置=").toLoc()
    }
}