package com.mcstarrysky.aiyatsbus.module.kether.action.operation

import com.mcstarrysky.aiyatsbus.core.util.TeamColorUtils
import org.bukkit.ChatColor
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.util.Vector
import taboolib.common.platform.function.submit
import kotlin.math.*

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.operation.Aiming
 *
 * @author mical
 * @since 2024/5/19 12:14
 */
object Aiming {
    fun shootBow(range: Double, ticks: Long, event: EntityShootBowEvent, color: ChatColor) {
        val arrow = event.projectile as AbstractArrow
        val who: LivingEntity = event.entity

        TeamColorUtils.getTeamByColor(color)?.addEntry(arrow.uniqueId.toString())
        arrow.isGlowing = true
        arrow.shooter = event.entity

        var target: LivingEntity? = (

                // 优先寻找玩家所指向的实体
                who.getTargetEntity(
                    range.roundToInt() * 2
                ) ?: who.getTargetBlockExact( // 如果没有，则根据距离找可用的实体
                    range.roundToInt() * 2
                )?.location?.getNearbyEntities(range, range, range)?.firstOrNull()

                )?.let {
                if (
                    it.uniqueId !== who.uniqueId
                    && it is LivingEntity
                    && it !is ArmorStand
                    && who.hasLineOfSight(it)
                ) it else null
            } // 如果有预先瞄准则获取特定的目标并持续追踪

        submit(delay = 1L, period = ticks) {
            // 判断此箭货是否还在飞行
            if (arrow.isDead || arrow.isInBlock || target?.isDead == true) {
                cancel()
                target?.isGlowing = false
                return@submit
            }

            // 分成两个 else 而不是一个是为了找到后就立即修正一次方向
            if (target == null) {
                target = arrow.getNearbyEntities(range, range, range).firstOrNull {
                    it.uniqueId !== who.uniqueId
                            // && !PermissionUtils.checkIfIsNPC(entity)
                            && it is LivingEntity
                            && it !is ArmorStand
                            && who.hasLineOfSight(it)
                } as LivingEntity?
            }

            if (target != null) { // 如果找到了目标就继续冲
                target?.uniqueId?.let { TeamColorUtils.getTeamByColor(color)?.addEntry(it.toString()) }
                target?.isGlowing = true

                val perfectDirection: Vector = arrow.location.clone().subtract(target!!.eyeLocation).toVector()
                perfectDirection.normalize()
                perfectDirection.multiply(-1)
                arrow.velocity = perfectDirection.multiply(0.5)
            }
        }
    }
}