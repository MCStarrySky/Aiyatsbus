package com.mcstarrysky.aiyatsbus.core.util

import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import java.util.*

object PermissionChecker {

    val checkingBlocks = mutableMapOf<Pair<String, UUID>, Boolean>()
    val checkingDamages = mutableMapOf<Pair<UUID, UUID>, Boolean>()

    fun hasBlockPermission(player: Player, block: Block): Boolean {
        val event = BlockBreakEvent(block, player)
        val pair = block.location.serialized to player.uniqueId
        checkingBlocks[pair] = true
        Bukkit.getPluginManager().callEvent(event)
        return checkingBlocks.remove(pair)!!
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, ignoreCancelled = false)
    fun event(event: BlockBreakEvent) {
        val loc = event.block.location.serialized
        val uuid = event.player.uniqueId
        if (checkingBlocks[loc to uuid] != null) {
            checkingBlocks[loc to uuid] = !event.isCancelled
            event.isCancelled = true
        }
    }

    fun isChecking(event: Event): Boolean {
        if (event is BlockBreakEvent)
            return checkingBlocks.containsKey(event.block.location.serialized to event.player.uniqueId)
        if (event is EntityDamageByEntityEvent)
            return checkingDamages.containsKey(event.damager.uniqueId to event.entity.uniqueId)
        return false
    }

    fun hasDamagePermission(damager: Entity, damaged: LivingEntity): Boolean {
        val event = EntityDamageByEntityEvent(damager, damaged, EntityDamageEvent.DamageCause.CUSTOM, 0.0)
        val pair = damager.uniqueId to damaged.uniqueId
        checkingDamages[pair] = true
        Bukkit.getPluginManager().callEvent(event)
        val flag = checkingDamages[pair]!!
        checkingDamages.remove(pair)
        return flag
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, ignoreCancelled = false)
    fun event(event: EntityDamageByEntityEvent) {
        val damagerUUID = event.damager.uniqueId
        val damagedUUID = event.entity.uniqueId
        if (checkingDamages[damagerUUID to damagedUUID] != null) {
            checkingDamages[damagerUUID to damagedUUID] = !event.isCancelled
            event.isCancelled = true
        }
    }
}