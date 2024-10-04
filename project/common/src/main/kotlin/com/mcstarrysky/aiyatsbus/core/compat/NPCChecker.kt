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

import org.bukkit.entity.Entity
import java.util.LinkedList

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.compat.NPCChecker
 *
 * @author mical
 * @date 2024/9/4 20:47
 */
interface NPCChecker {

    /**
     * 检查是否为 NPC
     */
    fun checkIfIsNPC(entity: Entity): Boolean

    companion object {

        val registeredIntegrations = LinkedList<NPCChecker>()

        /**
         * 检查是否为 NPC
         */
        fun checkIfIsNPC(entity: Entity): Boolean {
            return registeredIntegrations.isNotEmpty() &&
                    registeredIntegrations.any { it.checkIfIsNPC(entity) }
        }
    }
}