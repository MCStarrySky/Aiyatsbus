package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit

import com.mcstarrysky.aiyatsbus.core.util.isDay
import com.mcstarrysky.aiyatsbus.core.util.isNight
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.World
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.PropertyWorld
 *
 * @author mical
 * @since 2024/3/10 13:20
 */
@AiyatsbusProperty(
    id = "world",
    bind = World::class
)
class PropertyWorld : AiyatsbusGenericProperty<World>("world") {

    override fun readProperty(instance: World, key: String): OpenResult {
        val property: Any? = when (key) {
            "canGenerateStructures", "can-generate-structures" -> instance.canGenerateStructures()
            "allowAnimals", "allow-animals" -> instance.allowAnimals
            "allowMonsters", "allow-monsters" -> instance.allowMonsters
            "biomeProvider", "biome-provider" -> instance.biomeProvider
            "clearWeatherDuration", "clear-weather-duration" -> instance.clearWeatherDuration
            "difficulty" -> instance.difficulty
            "enderDragonBattle", "ender-dragon-battle" -> instance.enderDragonBattle
            "entities" -> instance.entities
            "featureFlags", "feature-flags" -> instance.featureFlags
            "forceLoadedChunks", "force-loaded-chunks" -> instance.forceLoadedChunks
            "fullTime", "full-time" -> instance.fullTime
            "gameRules", "game-rules" -> instance.gameRules
            "gameTime", "game-time" -> instance.gameTime
            "generator" -> instance.generator
            "keepSpawnInMemory", "keep-spawn-in-memory" -> instance.keepSpawnInMemory
            "livingEntities", "living-entities" -> instance.livingEntities
            "loadedChunks", "loaded-chunks" -> instance.loadedChunks
            "logicalHeight", "logical-height" -> instance.logicalHeight
            "players" -> instance.players
            "pluginChunkTickets", "plugin-chunk-tickets" -> instance.pluginChunkTickets
            "populators" -> instance.populators
            "pvp" -> instance.pvp
            "raids" -> instance.raids
            "seaLevel", "sea-level" -> instance.seaLevel
            "simulationDistance", "simulation-distance" -> instance.simulationDistance
            "spawnLocation", "spawn-location" -> instance.spawnLocation
            "thunderDuration", "thunder-duration" -> instance.thunderDuration
            "time" -> instance.time
            "viewDistance", "view-distance" -> instance.viewDistance
            "weatherDuration", "weather-duration" -> instance.weatherDuration
            "worldBorder", "world-border" -> instance.worldBorder
            "worldFolder", "world-folder" -> instance.worldFolder
            "hasCelling", "has-celling" -> instance.hasCeiling()
            "hasRaids", "has-raids" -> instance.hasRaids()
            "hasSkyLight", "has-sky-light" -> instance.hasSkyLight()
            "hasStorm", "has-storm" -> instance.hasStorm()
            "isAutoSave", "is-auto-save" -> instance.isAutoSave
            "isBedWorks", "is-bed-works" -> instance.isBedWorks
            "isClearWeather", "is-clear-weather" -> instance.isClearWeather
            "isHardcore", "is-hardcore" -> instance.isHardcore
            "isNatural","is-natural" -> instance.isNatural
            "isPiglinSafe","is-piglin-safe" -> instance.isPiglinSafe
            "isRespawnAnchorWorks", "is-respawn-anchor-works" -> instance.isRespawnAnchorWorks
            "isThundering", "is-thundering" -> instance.isThundering
            "isUltraWarm", "is-ultra-warm" -> instance.isUltraWarm

            "isDay", "is-day" -> instance.isDay
            "isNight", "is-night" -> instance.isNight
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: World, key: String, value: Any?): OpenResult {
        // TODO
        return OpenResult.failed()
    }
}