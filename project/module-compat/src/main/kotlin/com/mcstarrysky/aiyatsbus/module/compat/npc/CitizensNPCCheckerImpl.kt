package com.mcstarrysky.aiyatsbus.module.compat.npc

import com.mcstarrysky.aiyatsbus.core.compat.NPCChecker
import net.citizensnpcs.api.CitizensAPI
import org.bukkit.entity.Entity
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.compat.npc.CitizensNPCCheckerImpl
 *
 * @author mical
 * @date 2024/9/4 20:51
 */
object CitizensNPCCheckerImpl : NPCChecker {

    override fun checkIfIsNPC(entity: Entity): Boolean {
        return CitizensAPI.getNPCRegistry().isNPC(entity)
    }

    @Awake(LifeCycle.ACTIVE)
    fun init() {
        if (kotlin.runCatching {
            Class.forName("net.citizensnpcs.api.CitizensAPI")
        }.isSuccess) {
            NPCChecker.registeredIntegrations += this
        }
    }
}