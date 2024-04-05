package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.entity

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.entity.ProjectileHitEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.entity.PropertyProjectileHitEvent
 *
 * @author mical
 * @since 2024/4/5 21:08
 */
@AiyatsbusProperty(
    id = "projectile-hit-event",
    bind = ProjectileHitEvent::class
)
class PropertyProjectileHitEvent : AiyatsbusGenericProperty<ProjectileHitEvent>("projectile-hit-event") {

    override fun readProperty(instance: ProjectileHitEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "hitBlock", "hit-block" -> instance.hitBlock
            "hitBlockFace", "hit-block-face" -> instance.hitBlockFace
            "hitEntity", "hit-entity" -> instance.hitEntity
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: ProjectileHitEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}