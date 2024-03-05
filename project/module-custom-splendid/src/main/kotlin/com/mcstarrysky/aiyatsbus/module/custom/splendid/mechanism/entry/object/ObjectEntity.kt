package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.`object`

import com.mcstarrysky.aiyatsbus.core.util.toLoc
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.module.nms.getI18nName
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.ObjectEntry
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objString
import taboolib.common5.cbool
import taboolib.platform.util.sendLang
import java.util.*

object ObjectEntity : ObjectEntry<Entity>() {

    override fun modify(
        obj: Entity,
        cmd: String,
        params: List<String>
    ): Boolean {
        when (cmd) {
            "设置下蹲" -> obj.isSneaking = params[0].cbool
            "传送" -> obj.teleport(params[0].toLoc())
            "发送信息" -> {
                val tmp = if (params.size > 1) {
                    buildList<Pair<String, Any>> {
                        for (i in 1 until params.size step 2)
                            this += params[i] to params[i + 1]
                    }.toTypedArray()
                } else arrayOf()
                obj.sendLang(params[0], *tmp)
            }
        }
        return true
    }

    override fun get(from: Entity, objName: String): Pair<ObjectEntry<*>, Any?> {
        return when (objName) {
            "在下蹲" -> objString.h(from.isSneaking)
            "下落高度" -> objString.h(from.fallDistance)
            "名称" -> objString.h(from.customName() ?: from.getI18nName())
            "在空中" -> objString.h(!from.isOnGround)
            else -> runCatching {
                objString.h(from.invokeMethod(objName)) // 可以直接填写 "isOnGround" 此类
            }.getOrElse { objString.h(null) }
        }
    }

    override fun holderize(obj: Entity) = this to "${obj.uniqueId}"

    override fun disholderize(holder: String) = Bukkit.getEntity(UUID.fromString(holder))
}