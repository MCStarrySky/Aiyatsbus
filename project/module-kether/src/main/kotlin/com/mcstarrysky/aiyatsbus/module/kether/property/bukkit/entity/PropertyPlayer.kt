/*
 * This file is part of Vulpecula, licensed under the MIT License.
 *
 *  Copyright (c) 2018 Bkm016
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity

import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
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
            "bedLocation", "bed-location", "bed-loc", "bed" -> instance.bedSpawnLocation
            "viewDistance", "view-distance" -> instance.clientViewDistance
            "compassTarget", "compass-target", "compass" -> instance.compassTarget
            "displayName", "display-name", "display" -> instance.displayName()
            "experience", "exp" -> instance.exp
            "level" -> instance.level
            "flySpeed", "fly-speed" -> instance.flySpeed
            "healthScale", "health-scale" -> instance.healthScale
            "locale" -> instance.locale()
            "ping" -> instance.ping
            "playerListFooter", "player-list-footer" -> instance.playerListFooter()
            "playerListHeader", "player-list-header" -> instance.playerListHeader()
            "playerListName", "player-list-name", "player-list" -> instance.playerListName()

            "playerTime", "time" -> instance.playerTime
            "playerTimeOffset", "time-offset" -> instance.playerTimeOffset
            "isPlayerTimeRelative", "time-relative" -> instance.isPlayerTimeRelative
            "isSprinting", "is-sprinting", "sprinting" -> instance.isSprinting

            "playerWeather", "weather" -> instance.playerWeather

            "previousGameMode", "previous-game-mode", "previous-gamemode", "gamemode-previous" -> instance.previousGameMode
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Player, key: String, value: Any?): OpenResult {
        when (key) {
            "bedSpawnLocation", "bed-location", "bed-loc", "bed" -> instance.bedSpawnLocation = value as? Location ?: return OpenResult.successful()
            "compassTarget", "compass-target", "compass" -> instance.compassTarget = value as? Location ?: return OpenResult.successful()
            "experience", "exp" -> instance.exp = value?.cfloat ?: return OpenResult.successful()
            "level" -> instance.level = value?.cint ?: return OpenResult.successful()
            "flySpeed", "fly-speed" -> instance.flySpeed = value?.cfloat ?: return OpenResult.successful()
            "healthScale", "health-scale" -> instance.healthScale = value?.cdouble ?: return OpenResult.successful()

            "playerListFooter", "player-list-footer" -> instance.playerListFooter = value?.toString() ?: return OpenResult.successful()
            "playerListHeader", "player-list-header" -> instance.playerListHeader = value?.toString() ?: return OpenResult.successful()
            "playerTime", "time" -> instance.setPlayerTime(value?.clong ?: return OpenResult.successful(), false)
            "playerWeather", "weather" -> instance.setPlayerWeather(value?.toString()?.let{WeatherType.valueOf(it)} ?: return OpenResult.successful())
            "isSprinting", "is-sprinting", "sprinting" -> instance.isSprinting = value?.coerceBoolean() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}