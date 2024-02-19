package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.`object`

import com.mcstarrysky.aiyatsbus.core.util.*
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.function.submit
import taboolib.module.nms.getI18nName
import taboolib.platform.util.groundBlock
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.*
import java.util.*
import kotlin.math.pow

object ObjectLivingEntity : ObjectEntry<LivingEntity>() {

    override fun modify(
        obj: LivingEntity,
        cmd: String,
        params: List<String>
    ): Boolean {
        objEntity.modify(obj, cmd, params)
        when (cmd) {
            "施加药水效果" -> {
                val type = PotionEffectType.values().find { it.name == params[0] || it.getI18nName() == params[0] } ?: return true
                obj.effect(type, params[1].calcToInt(), params[2].calcToInt())
            }

            "霹雷" -> {
                obj.world.strikeLightningEffect(obj.location)
                obj.realDamage((params[0, "4.0"]).calcToDouble())
            }

            "真实伤害" -> {
                val dmg = params[0].calcToDouble()
                params.getOrNull(1)?.let {
                    obj.realDamage(dmg, objPlayer.disholderize(it))
                } ?: obj.realDamage(dmg)
            }

            "伤害" -> obj.damage(params[0].calcToDouble(), objPlayer.disholderize(params[1]))

            "设置血量" -> obj.health = params[0].calcToDouble()

            "设置伤害吸收量" -> obj.absorptionAmount = params[0].calcToDouble()

            "弹飞" -> {
                val height = params[0].calcToDouble()
                val y = 0.1804 * height - 0.0044 * height.pow(2) + 0.00004 * height.pow(3)
                val vector = obj.velocity.also { it.y = 0.0; it.add(vector(0, y, 0)) }
                submit {
                    obj.velocity = vector
                }
            }

            "施加速度" -> {
                obj.velocity = objVector.disholderize(params[0])
            }
        }
        return true
    }

    override fun get(from: LivingEntity, objName: String): Pair<ObjectEntry<*>, Any?> {
        if (objName.startsWith("周围生物")) {
            val range = objName.numbers[0]
            return objLivingEntity to from.getNearbyEntities(range, range, range)
                .filterIsInstance<LivingEntity>()
                .map { objLivingEntity.h(it) }
        }
        return when (objName) {
            "血量" -> objString.h(from.health)
            "最大血量" -> objString.h(from.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value)
            "伤害吸收值" -> objString.h(from.absorptionAmount)
            "脚下方块" -> objBlock.holderize(from.blockBelow ?: from.groundBlock)
            "朝向向量" -> objVector.holderize(from.eyeLocation.direction.normalize())
            else -> objEntity[from, objName]
        }
    }

    override fun holderize(obj: LivingEntity) = this to "${obj.uniqueId}"

    override fun disholderize(holder: String) = Bukkit.getEntity(UUID.fromString(holder)) as? LivingEntity
}