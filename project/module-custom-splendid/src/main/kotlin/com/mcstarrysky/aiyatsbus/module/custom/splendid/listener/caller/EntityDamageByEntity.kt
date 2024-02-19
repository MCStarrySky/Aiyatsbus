package com.mcstarrysky.aiyatsbus.module.custom.splendid.listener.caller

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent
import com.mcstarrysky.aiyatsbus.core.fixedEnchants
import com.mcstarrysky.aiyatsbus.core.util.FurtherOperation
import com.mcstarrysky.aiyatsbus.core.util.PermissionChecker
import com.mcstarrysky.aiyatsbus.core.util.mainHand
import com.mcstarrysky.aiyatsbus.module.custom.splendid.SplendidTrigger
import com.mcstarrysky.aiyatsbus.module.custom.splendid.TriggerSlots
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.EventType
import com.mcstarrysky.aiyatsbus.module.custom.splendid.triggerEts
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.entity.Trident
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.platform.util.attacker
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object EntityDamageByEntity {

    val projectileSourceItems = ConcurrentHashMap<UUID, ItemStack>()

    @SubscribeEvent(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun shoot(event: EntityShootBowEvent) {
        val handItem = event.entity.equipment?.getItem(event.hand)
        projectileSourceItems[event.projectile.uniqueId] = event.bow ?: handItem ?: return
    }

    @SubscribeEvent(priority = EventPriority.MONITOR)
    fun remove(event: EntityRemoveFromWorldEvent) = projectileSourceItems.remove(event.entity.uniqueId)

    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun lowest(event: EntityDamageByEntityEvent) = settle(event, EventPriority.LOWEST)

    @SubscribeEvent(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun high(event: EntityDamageByEntityEvent) = settle(event, EventPriority.HIGH)

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun highest(event: EntityDamageByEntityEvent) = settle(event, EventPriority.HIGHEST)

    private fun settle(event: EntityDamageByEntityEvent, priority: EventPriority) {
        if (PermissionChecker.isChecking(event)) return
        if (FurtherOperation.hadOperated(event)) return
        FurtherOperation.addStamp(event)

        val attacker = event.attacker ?: return
        val damager = event.damager
        val damaged = event.entity
        val isProjectile = damager is Projectile

        if (damaged is ArmorStand || damaged !is LivingEntity) return

        val weapon =
            if (isProjectile)
                if (damager is Trident) damager.item
                else projectileSourceItems[damager.uniqueId]
            else (attacker.mainHand() ?: return)

        weapon ?: return
        if ((weapon.type == Material.BOW || weapon.type == Material.CROSSBOW) && !isProjectile) return

        weapon.fixedEnchants.forEach { (enchant, _) ->
            (enchant.trigger as? SplendidTrigger ?: return).listeners.trigger(event, EventType.ATTACK, priority, attacker, weapon, EquipmentSlot.HAND)
        }
        EventType.ATTACK.triggerEts(event, priority, TriggerSlots.ARMORS, attacker)
        EventType.ATTACK.triggerEts(event, priority, TriggerSlots.OFF_HAND, attacker)

        FurtherOperation.delStamp(event)

        submit {
            if (damaged.isDead) {
                weapon.fixedEnchants.forEach { (enchant, _) ->
                    (enchant.trigger as? SplendidTrigger ?: return@forEach).listeners.trigger(event, EventType.KILL, priority, attacker, weapon, EquipmentSlot.HAND)
                }
                EventType.KILL.triggerEts(event, priority, TriggerSlots.ARMORS, attacker)
                EventType.KILL.triggerEts(event, priority, TriggerSlots.OFF_HAND, attacker)
            }
        }
    }
}