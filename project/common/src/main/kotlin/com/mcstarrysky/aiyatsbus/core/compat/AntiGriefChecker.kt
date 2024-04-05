package com.mcstarrysky.aiyatsbus.core.compat

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.compat.AntiGriefChecker
 *
 * @author mical
 * @since 2024/3/21 21:56
 */
object AntiGriefChecker {
    private val registeredChecker = hashSetOf<AntiGrief>() // 已注册的检查
    private val checkers = hashSetOf<AntiGrief>() // 缓存可用的检查

    fun canPlace(player: Player, location: Location): Boolean {
        return checkers.all { it.canPlace(player, location) }
    }

    fun canBreak(player: Player, location: Location): Boolean {
        return checkers.all { it.canBreak(player, location) }
    }

    fun canInteract(player: Player, location: Location): Boolean {
        return checkers.all { it.canInteract(player, location) }
    }

    fun canInteractEntity(player: Player, entity: Entity): Boolean {
        return checkers.all { it.canInteractEntity(player, entity) }
    }

    fun canDamage(player: Player, entity: Entity): Boolean {
        return checkers.all { it.canDamage(player, entity) }
    }

    fun registerNewCompatibility(comp: AntiGrief) {
        registeredChecker += comp

        if (comp.checkRunning()) {
            checkers += comp
        } // 这时候肯定可以读到了, 先处理一次
    }

    @SubscribeEvent
    fun e(e: PluginEnableEvent) {
        val checker = registeredChecker
            .find { it.getAntiGriefPluginName() == e.plugin.name } ?: return
        checkers += checker
    }

    @SubscribeEvent
    fun e(e: PluginDisableEvent) {
        checkers.removeAll {
            it.getAntiGriefPluginName() == e.plugin.name
        }
    }
}