package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.`object`

import com.mcstarrysky.aiyatsbus.core.util.add
import com.mcstarrysky.aiyatsbus.core.util.calcToDouble
import com.mcstarrysky.aiyatsbus.core.util.numbers
import org.bukkit.util.Vector
import taboolib.common5.cdouble
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.ObjectEntry
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objString
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objVector

object ObjectVector : ObjectEntry<Vector>() {

    override fun modify(obj: Vector, cmd: String, params: List<String>): Boolean {
        when (cmd) {
            "设为零向量" -> obj.zero()
            "单位向量" -> obj.normalize()
            "数乘" -> obj.multiply(params[0].calcToDouble())
            "数除" -> obj.multiply(params[0].calcToDouble())
            "数加" -> obj.add(params[0].calcToDouble(), params[1].calcToDouble(), params[2].calcToDouble())
            "数减" -> obj.add(-params[0].calcToDouble(), -params[1].calcToDouble(), -params[2].calcToDouble())
            "加" -> obj.add(objVector.disholderize(params[0]))
            "减" -> obj.subtract(objVector.disholderize(params[0]))
            "x" -> obj.x = params[0].calcToDouble()
            "y" -> obj.y = params[0].calcToDouble()
            "z" -> obj.z = params[0].calcToDouble()
            "在AABB盒" -> return obj.isInAABB(objVector.d(params[0]) ?: return false, objVector.d(params[1]) ?: return false) // 没搞懂变量具体格式如何写, 不知道能否用在这里
            "在球形空间内" -> return obj.isInSphere(objVector.d(params[0]) ?: return false, params[1].cdouble)
        }
        return true
    }

    override fun get(from: Vector, objName: String): Pair<ObjectEntry<*>, Any?> {
        return when (objName) {
            "复制向量" -> objVector.holderize(from.clone())
            "是否为零向量" -> objString.h(from.isZero)
            "是否为单位向量" -> objString.h(from.isNormalized)
            "x" -> objString.h(from.x)
            "y" -> objString.h(from.y)
            "z" -> objString.h(from.z)
            "取整x" -> objString.h(from.blockX)
            "取整y" -> objString.h(from.blockY)
            "取整z" -> objString.h(from.blockZ)
            else -> runCatching {
                objString.h(from.invokeMethod(objName)) // 可以直接填写 "isOnGround" 此类
            }.getOrElse { objString.h(null) }
        }
    }

    override fun holderize(obj: Vector): Pair<ObjectEntry<Vector>, String> {
        return this to "向量=${obj.x},${obj.y},${obj.z}"
    }

    override fun disholderize(holder: String): Vector {
        // MCStarrySky - 这里没搞明白, 为什么不手动解析?
        // val (x, y, z) = holder.removePrefix("向量=").split(',', limit = 3).map(String::cdouble)
        // return Vector(x, y, z)
        val numbers = holder.numbers
        return Vector(numbers[0], numbers[1], numbers[2])
    }
}