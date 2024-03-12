@file:Suppress("deprecation")

package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.WeatherType
import org.bukkit.entity.Player
import taboolib.common.OpenResult
import taboolib.common5.cdouble
import taboolib.common5.cfloat
import taboolib.common5.cint
import taboolib.common5.clong

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity.PropertyPlayer
 *
 * @author Lanscarlos
 * @since 2023-04-13 09:51
 */
@AiyatsbusProperty(
    id = "player",
    bind = Player::class
)
class PropertyPlayer : AiyatsbusGenericProperty<Player>("player") {

    override fun readProperty(instance: Player, key: String): OpenResult {
        val property: Any? = when (key) {
            "name" -> instance.name
            "bed-location", "bed-loc", "bed" -> instance.bedSpawnLocation
            "uuid" -> instance.uniqueId.toString()
            "view-distance" -> instance.clientViewDistance
            "compass-target", "compass" -> instance.compassTarget
            "display-name", "display" -> instance.displayName
            "exp" -> instance.exp
            "level" -> instance.level
            "fly-speed" -> instance.flySpeed
            "health-scale" -> instance.healthScale
            "locale" -> instance.locale
            "ping" -> instance.ping
            "player-list-footer" -> instance.playerListFooter
            "player-list-header" -> instance.playerListHeader
            "player-list-name", "player-list" -> instance.playerListName

            "time" -> instance.playerTime
            "time-offset" -> instance.playerTimeOffset
            "time-relative" -> instance.isPlayerTimeRelative

            "weather" -> instance.playerWeather

            "game-mode", "gamemode" -> instance.gameMode
            "previous-game-mode", "previous-gamemode", "gamemode-previous" -> instance.previousGameMode
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Player, key: String, value: Any?): OpenResult {
        when (key) {
            "bed-location", "bed-loc", "bed" -> {
                instance.bedSpawnLocation = value as? Location ?: return OpenResult.successful()
            }
            "compass-target", "compass" -> {
                instance.compassTarget = value as? Location ?: return OpenResult.successful()
            }
            "exp" -> {
                instance.exp = value?.cfloat ?: return OpenResult.successful()
            }
            "level" -> {
                instance.level = value?.cint ?: return OpenResult.successful()
            }
            "fly-speed" -> {
                instance.flySpeed = value?.cfloat ?: return OpenResult.successful()
            }
            "health-scale" -> {
                instance.healthScale = value?.cdouble ?: return OpenResult.successful()
            }
            "player-list-footer" -> {
                instance.playerListFooter = value?.toString() ?: return OpenResult.successful()
            }
            "player-list-header" -> {
                instance.playerListHeader = value?.toString() ?: return OpenResult.successful()
            }
            "time" -> {
                instance.setPlayerTime(value?.clong ?: return OpenResult.successful(), false)
            }
            "weather" -> {
                instance.setPlayerWeather(value?.toString()?.let { WeatherType.valueOf(it) }
                    ?: return OpenResult.successful())
            }
            "game-mode", "gamemode" -> {
                instance.gameMode = value?.toString()?.let { GameMode.valueOf(it) }
                    ?: return OpenResult.successful()
            }
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }

}