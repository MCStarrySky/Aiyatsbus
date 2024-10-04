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
package com.mcstarrysky.aiyatsbus.core.event

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.event.AiyatsbusPrepareAnvilEvent
 *
 * @author mical
 * @since 2024/5/3 16:02
 */
class AiyatsbusPrepareAnvilEvent(val left: ItemStack, val right: ItemStack?, var result: ItemStack?, var name: String?, val player: Player) : BukkitProxyEvent() {

    override val allowCancelled: Boolean = true
}