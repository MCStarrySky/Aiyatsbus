package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveEntity
import org.bukkit.entity.Projectile
import org.bukkit.projectiles.ProjectileSource
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity.PropertyProjectile
 *
 * @author mical
 * @since 2024/3/24 21:23
 */
@AiyatsbusProperty(
    id = "projectile",
    bind = Projectile::class
)
class PropertyProjectile : AiyatsbusGenericProperty<Projectile>("projectile") {

    override fun readProperty(instance: Projectile, key: String): OpenResult {
        val property: Any? = when (key) {
            "shooter" -> instance.shooter
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Projectile, key: String, value: Any?): OpenResult {
        when (key) {
            "shooter" -> {
                instance.shooter = (value?.liveEntity as ProjectileSource?)
                    ?: return OpenResult.successful()
            }
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}