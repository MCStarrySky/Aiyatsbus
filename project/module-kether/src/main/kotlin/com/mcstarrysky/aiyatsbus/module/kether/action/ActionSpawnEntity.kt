package com.mcstarrysky.aiyatsbus.module.kether.action

import org.bukkit.Location
import org.bukkit.entity.EntityType
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionSpawnEntity
 *
 * @author mical
 * @since 2024/4/4 11:09
 */
object ActionSpawnEntity {

    @KetherParser(["spawn-entity"])
    fun spawnEntityParser() = combinationParser {
        it.group(text(), command("at", then = type<Location>())).apply(it) { type, location ->
            now {
                try {
                    location.world?.spawnEntity(location, EntityType.valueOf(type.uppercase()))
                } catch (_: Throwable) {
                }
            }
        }
    }
}