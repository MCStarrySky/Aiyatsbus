package com.mcstarrysky.aiyatsbus.module.kether.property.taboolib

import com.mcstarrysky.aiyatsbus.core.util.coerceDouble
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import taboolib.common.OpenResult
import taboolib.common.util.Vector

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.taboolib.PropertyVector
 *
 * @author Lanscarlos
 * @since 2023-03-22 14:02
 */
@AiyatsbusProperty(
    id = "vector-taboo",
    bind = Vector::class
)
class PropertyVector : AiyatsbusGenericProperty<Vector>("vector-taboo") {

    override fun readProperty(instance: Vector, key: String): OpenResult {
        val property: Any = when (key) {
            "clone" -> instance.clone()
            "bukkit", "bk" -> org.bukkit.util.Vector(instance.x, instance.y, instance.z)
            "blockX", "block-x" -> instance.blockX
            "blockY", "block-y" -> instance.blockY
            "blockZ", "block-z" -> instance.blockZ
            "x" -> instance.x
            "y" -> instance.y
            "z" -> instance.z
            "length" -> instance.length()
            "lengthSquared", "length-squared", "length-sq", "squared", "sq" -> instance.lengthSquared()
            "isNormalized", "normalized" -> instance.isNormalized
            "normalize" -> instance.clone().normalize()
            "zero" -> instance.clone().zero()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Vector, key: String, value: Any?): OpenResult {
        when (key) {
            "x" -> {
                instance.x = value?.coerceDouble() ?: return OpenResult.successful()
            }
            "y" -> {
                instance.y = value?.coerceDouble() ?: return OpenResult.successful()
            }
            "z" -> {
                instance.z = value?.coerceDouble() ?: return OpenResult.successful()
            }
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}