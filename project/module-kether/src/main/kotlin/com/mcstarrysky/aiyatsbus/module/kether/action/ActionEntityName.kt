package com.mcstarrysky.aiyatsbus.module.kether.action

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.module.kether.player
import taboolib.module.nms.getI18nName

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionEntityName
 *
 * @author mical
 * @since 2024/3/24 11:30
 */
object ActionEntityName {

    @KetherParser(["entity-name"], shared = true)
    fun entityNameParser() = combinationParser {
        it.group(type<Entity>()).apply(it) { entity ->
            now {
                if (entity is Player) {
                    entity.name
                } else {
                    entity.customName ?: entity.getI18nName(player().castSafely())
                }
            }
        }
    }
}