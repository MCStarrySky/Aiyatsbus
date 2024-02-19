package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.`object`

import com.mcstarrysky.aiyatsbus.core.util.calcToBoolean
import com.mcstarrysky.aiyatsbus.core.util.calcToInt
import com.mcstarrysky.aiyatsbus.core.util.get
import com.mcstarrysky.aiyatsbus.core.util.takeItem
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.ObjectEntry
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objItem
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objLivingEntity
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objString
import java.util.*

object ObjectPlayer : ObjectEntry<Player>() {

    override fun modify(
        obj: Player,
        cmd: String,
        params: List<String>
    ): Boolean {
        objLivingEntity.modify(obj, cmd, params)
        when (cmd) {
            "设置飞行" -> obj.isFlying = params[0].calcToBoolean()
            "设置可飞行" -> obj.allowFlight = params[0].calcToBoolean()
            "扣除物品" -> return obj.takeItem(params[1, "1"].calcToInt()) { it.isSimilar(objItem.disholderize(params[0])) }
        }
        return true
    }

    override fun get(from: Player, objName: String): Pair<ObjectEntry<*>, Any?> {
        return when (objName) {
            "游戏模式" -> objString.h(from.gameMode)
            "在飞行" -> objString.h(from.isFlying)
            else -> objLivingEntity[from, objName]
        }
    }

    override fun holderize(obj: Player) = this to "${obj.uniqueId}"
    override fun disholderize(holder: String) = Bukkit.getPlayer(UUID.fromString(holder))
}