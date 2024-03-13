package com.mcstarrysky.aiyatsbus.module.listener.mechanism

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

@ConfigNode(bind = "mechanisms/exp.yml")
object ExpListener {

    @Config("mechanisms/exp.yml", autoReload = true)
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

    fun modified(level: Int) =
        expFormulas.lastOrNull { it.first <= level }?.second?.calcToInt("level" to level) ?: vanilla(level)

    fun vanilla(level: Int) = if (level <= 15) 2 * level + 7
    else if (level <= 30) 5 * level - 38
    else 9 * level - 158

    fun finalAttain(origin: Int, player: Player) = privilege.maxOf { (perm, expression) ->
        if (player.hasPermission(perm)) expression.calcToInt("exp" to origin)
        else origin
    }.coerceAtLeast(0)
}