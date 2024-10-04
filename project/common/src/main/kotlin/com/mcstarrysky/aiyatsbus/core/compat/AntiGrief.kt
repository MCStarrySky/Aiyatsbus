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

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.platform.util.bukkitPlugin

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.compat.AntiGrief
 *
 * @author mical
 * @since 2024/4/4 12:53
 */
interface AntiGrief {

    /**
     * 检查玩家是否可以放置方块
     */
    fun canPlace(player: Player, location: Location): Boolean

    /**
     * 检查玩家是否可以破坏方块
     */
    fun canBreak(player: Player, location: Location): Boolean

    /**
     * 检查玩家是否可以与方块交互
     */
    fun canInteract(player: Player, location: Location): Boolean

    /**
     * 检查玩家是否可以与实体交互
     */
    fun canInteractEntity(player: Player, entity: Entity): Boolean

    /**
     * 检查玩家是否可以伤害实体
     */
    fun canDamage(player: Player, entity: Entity): Boolean

    /**
     * 获取插件名称
     */
    fun getAntiGriefPluginName(): String

    /**
     * 检查插件是否存在
     */
    fun checkRunning(): Boolean {
        return bukkitPlugin.server.pluginManager.getPlugin(getAntiGriefPluginName()) != null
    }
}