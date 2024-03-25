package com.mcstarrysky.aiyatsbus.module.kether.action

import com.mcstarrysky.aiyatsbus.core.util.realDamage
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionRealDamage
 *
 * @author mical
 * @since 2024/3/25 21:14
 */
object ActionRealDamage {

    @KetherParser(["realDamage", "real-damage"])
    fun realDamageParser() = combinationParser {
        it.group(type<LivingEntity>(), command("with", then = double()), command("by", then = type<Entity>()).option()).apply(it) { entity, damage, who ->
            now {
                entity.realDamage(damage, who)
            }
        }
    }
}