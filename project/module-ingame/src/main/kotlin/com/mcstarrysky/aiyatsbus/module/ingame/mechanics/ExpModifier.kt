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
package com.mcstarrysky.aiyatsbus.module.ingame.mechanics

import com.mcstarrysky.aiyatsbus.core.util.calcToInt
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerExpChangeEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.conversion

@ConfigNode(bind = "core/mechanisms/exp.yml")
object ExpModifier {

    @Config("core/mechanisms/exp.yml", autoReload = true)
    lateinit var conf: Configuration
        private set

    @ConfigNode("enable")
    var enable: Boolean = false

    @delegate:ConfigNode("exp_per_level")
    val expFormulas by conversion<ConfigurationSection, List<Pair<Int, String>>> {
        mutableListOf(*getKeys(false).map { path -> path.toInt() to getString(path)!! }.toTypedArray())
    }

    @delegate:ConfigNode("privilege")
    val privilege by conversion<List<String>, Map<String, String>> {
        mapOf(*map { it.split(":")[0] to it.split(":")[1] }.toTypedArray())
    }

    @SubscribeEvent(priority = EventPriority.MONITOR)
    fun onExp(event: PlayerExpChangeEvent) {
        if (!enable)
            return

        val player = event.player
        val level = player.level
        val attained = finalAttain(event.amount, player)

        val percent = player.exp

        val expNeedToUpgrade = modified(level)
        val exp = percent * expNeedToUpgrade
        var newExp = exp + attained

        event.amount = 0

        submit {
            while (newExp >= modified(player.level)) newExp -= modified(player.level++)
            player.exp = newExp / modified(player.level)
        }
    }

    private fun modified(level: Int) =
        expFormulas.lastOrNull { it.first <= level }?.second?.calcToInt("level" to level) ?: vanilla(level)

    private fun vanilla(level: Int) = if (level <= 15) 2 * level + 7
    else if (level <= 30) 5 * level - 38
    else 9 * level - 158

    private fun finalAttain(origin: Int, player: Player) = privilege.maxOf { (perm, expression) ->
        if (player.hasPermission(perm)) expression.calcToInt("exp" to origin)
        else origin
    }.coerceAtLeast(0)
}