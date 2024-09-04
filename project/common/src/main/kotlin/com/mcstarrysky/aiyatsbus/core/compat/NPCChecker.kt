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