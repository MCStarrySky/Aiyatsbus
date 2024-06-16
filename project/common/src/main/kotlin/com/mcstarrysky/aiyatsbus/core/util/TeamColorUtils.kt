package com.mcstarrysky.aiyatsbus.core.util

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scoreboard.Team
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.TeamColorUtils
 *
 * @author 南外丶仓鼠
 * @since 2024/6/16 21:53
 */
object TeamColorUtils {

    private val coloredTeams = ConcurrentHashMap<ChatColor, Team?>()

    init {
        val manager = Bukkit.getScoreboardManager()
        val board = manager.mainScoreboard
        for (color in ChatColor.values()) {
            if (board.getTeam("Aiyatsbus-" + color.name) == null) {
                val team = board.registerNewTeam("Aiyatsbus-" + color.name)
                team.color = color
            }
            coloredTeams[color] = board.getTeam("Aiyatsbus-" + color.name)
        }
    }

    fun getTeamByColor(color: ChatColor): Team? {
        return coloredTeams[color]
    }
}
