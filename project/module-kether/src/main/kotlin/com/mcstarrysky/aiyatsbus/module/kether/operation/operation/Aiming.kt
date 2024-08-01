package com.mcstarrysky.aiyatsbus.module.kether.operation.operation

import com.mcstarrysky.aiyatsbus.core.util.*
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

    @Suppress("UNCHECKED_CAST")
    fun shootBow(args: List<Any?>?) {
        shootBow(
            args?.get(0).coerceDouble(),
            args?.get(1).coerceLong(),
            args?.get(2) as EntityShootBowEvent,
            ChatColor.values().firstOrNull { it.name.lowercase() == args[3].toString().lowercase() } ?: ChatColor.WHITE,
            args[4] as List<String>
        )
    }

    private fun shootBow(range: Double, ticks: Long, event: EntityShootBowEvent, color: ChatColor, blackList: List<String>) {
        val arrow = event.projectile as AbstractArrow
        val shooter: LivingEntity = event.entity

        TeamColorUtils.getTeamByColor(color)?.addEntry(arrow.uniqueId.toString())
        arrow.isGlowing = true
        arrow.shooter = event.entity
        arrow.setNoPhysics(true)

        val targetCriteria: (Entity) -> Boolean = {
            it is Mob &&
                    it.uniqueId !== shooter.uniqueId &&
                    // && !PermissionUtils.checkIfIsNPC(entity)
                    it.type.name.lowercase() !in blackList &&
                    !it.isInvulnerable &&
                    shooter.hasLineOfSight(it)
        }

        var target: Mob? = (
            // 优先寻找玩家所指向的实体
            shooter.getTargetEntity(
                range.roundToInt() * 2
            ) ?: shooter.getTargetBlockExact( // 如果没有，则根据距离找可用的实体
                range.roundToInt() * 2
            )?.location?.getNearbyEntities(range, range, range)?.firstOrNull()
        )?.let {
            if (targetCriteria(it)) it as? Mob else null
        } // 如果有预先瞄准则获取特定的目标并持续追踪


        submit(delay = 1L, period = ticks) {
            // 判断此箭货是否还在飞行
            if (
                arrow.isDead ||
                arrow.isInBlock ||
                target?.isDead == true ||
                arrow.velocity.length() <= 0.5 // 速度太慢直接不追踪，防止追着打
            ) {
                arrow.isGlowing = false
                target?.isGlowing = false
                arrow.setNoPhysics(false)
                cancel()
                return@submit
            }

            // 分成两个 else 而不是一个是为了找到后就立即修正一次方向
            if (target == null) {
                target = arrow.getNearbyEntities(range, range, range).firstOrNull(targetCriteria) as? Mob
            }

            // 如果找到了目标就继续冲
            target?.let {
                TeamColorUtils.getTeamByColor(color)?.addEntry(it.uniqueId.toString())
                it.isGlowing = true

                val perfectDirection: Vector = arrow.location.clone()
                    .subtract(it.eyeLocation)
                    .toVector()
                    .normalize()
                    .multiply(-1)
                // 设置箭速
                VectorUtils.addVelocity(arrow, perfectDirection.multiply(arrow.velocity.length()), false)
            }
        }
    }
}