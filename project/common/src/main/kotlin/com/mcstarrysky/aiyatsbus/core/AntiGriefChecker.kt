package com.mcstarrysky.aiyatsbus.core

import net.momirealms.antigrieflib.AbstractComp
import net.momirealms.antigrieflib.AntiGriefLib
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.util.unsafeLazy
import taboolib.platform.util.bukkitPlugin

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AntiGriefChecker
 *
 * @author mical
 * @since 2024/3/21 21:56
 */
object AntiGriefChecker {

    private val handle: AntiGriefLib by unsafeLazy {
        AntiGriefLib.builder(bukkitPlugin)
            .silentLogs(true)
            .ignoreOP(AiyatsbusSettings.antiGriefIgnoreOp)
            .build()
    }

    @Awake(LifeCycle.ACTIVE)
    fun init() {
        handle.init()
    }

    fun canBreak(player: Player, location: Location): Boolean {
        return handle.canBreak(player, location)
    }

    fun canDamage(player: Player, entity: Entity): Boolean {
        return handle.canDamage(player, entity)
    }

    fun registerNewCompatibility(comp: AbstractComp, init: Boolean = true) {
        if (init) {
            comp.init()
        }
        handle.registerNewCompatibility(comp)
    }
}