package com.mcstarrysky.aiyatsbus.core.util

import com.mcstarrysky.aiyatsbus.core.StandardPriorities
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.common.platform.function.submit
import java.util.*

object FurtherOperation {

    // 游戏刻时间戳 to 是否已经处理过了
    //伤害者
    val lastDamageTracker = mutableMapOf<UUID, Int>()
    val lastBreakTracker = mutableMapOf<UUID, Int>()

    @Awake(LifeCycle.CONST)
    fun load() {
        registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.FURTHER_OPERATION) {
            submit(delay = 0L, period = 20L) {
                val tick = Bukkit.getCurrentTick()
                lastDamageTracker.values.removeIf { tick - it >= 20 }
                lastBreakTracker.values.removeIf { tick - it >= 20 }
            }
        }
    }

    fun addStamp(event: Event) {
        if (event is EntityDamageByEntityEvent)
            lastDamageTracker[event.damager.uniqueId] = Bukkit.getCurrentTick()
        if (event is BlockBreakEvent)
            lastBreakTracker[event.player.uniqueId] = Bukkit.getCurrentTick()
    }

    fun delStamp(event: Event) {
        if (event is EntityDamageByEntityEvent)
            lastDamageTracker.remove(event.damager.uniqueId)
        if (event is BlockBreakEvent)
            lastBreakTracker.remove(event.player.uniqueId)
    }

    fun hadOperated(event: Event): Boolean {
        if (event is EntityDamageByEntityEvent)
            lastDamageTracker[event.damager.uniqueId]?.let {
                if (it >= Bukkit.getCurrentTick() - 1)
                    return true
            }
        if (event is BlockBreakEvent)
            lastDamageTracker[event.player.uniqueId]?.let {
                if (it >= Bukkit.getCurrentTick() - 1)
                    return true
            }
        return false
    }

    //尝试追加新的伤害，但是不迭代引发伤害监听器
    fun furtherDamage(damager: Entity, damaged: LivingEntity, damage: Double) {
        if (PermissionChecker.hasDamagePermission(damager, damaged)) {
            damaged.damage(damage, damager)
        }
    }

    //后面两个参数因附魔而异
    //比如较大面积的一次性破坏方块，triggereffects应该为false，否则会导致玩家卡顿
    fun furtherBreak(player: Player?, block: Block) {
        if (player == null) return
        else if (PermissionChecker.hasBlockPermission(player, block)) player.breakBlock(block)
    }

    fun furtherPlace(player: Player?, block: Block, type: Material) {
        if (block.type != Material.AIR) return
        if (player == null) return
        if (PermissionChecker.hasBlockPermission(player, block)) {
            block.type = type
            //TODO 应当呼应事件给其他插件处理
        }
    }
}