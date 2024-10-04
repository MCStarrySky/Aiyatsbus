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
package com.mcstarrysky.aiyatsbus.module.compat.chat

import net.kyori.adventure.text.Component
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.compat.vanilla.ListenerPlayerDeath
 *
 * @author mical
 * @since 2024/3/17 12:25
 */
object ListenerPlayerDeath {

    @SubscribeEvent
    fun e(e: PlayerDeathEvent) {
        // 支持死亡界面显示那个物品
        e.deathMessage(DisplayReplacer.inst.apply(e.deathMessage() ?: return, e.entity) as Component)
    }
}