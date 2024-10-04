/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.mcstarrysky.aiyatsbus.core.compat

import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent
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

    fun canPlace(player: Player, location: Location): Boolean =
        checkPermission(player) { it.canPlace(player, location) }

    fun canBreak(player: Player, location: Location): Boolean =
        checkPermission(player) { it.canBreak(player, location) }

    fun canInteract(player: Player, location: Location): Boolean =
        checkPermission(player) { it.canInteract(player, location) }

    fun canInteractEntity(player: Player, entity: Entity): Boolean =
        checkPermission(player) { it.canInteractEntity(player, entity) }

    fun canDamage(player: Player, entity: Entity): Boolean =
        checkPermission(player) { it.canDamage(player, entity) }

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

    private inline fun checkPermission(player: Player, action: (AntiGrief) -> Boolean): Boolean {
        if (player.isOp && AiyatsbusSettings.antiGriefIgnoreOp) return true
        return checkers.all { action(it) }
    }
}