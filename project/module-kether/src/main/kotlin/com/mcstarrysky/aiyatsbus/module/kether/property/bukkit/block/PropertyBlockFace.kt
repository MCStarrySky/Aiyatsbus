package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.block

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.block.BlockFace
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.block.PropertyBlockFace
 *
 * @author mical
 * @since 2024/4/5 21:12
 */
@AiyatsbusProperty(
    id = "block-face",
    bind = BlockFace::class
)
class PropertyBlockFace : AiyatsbusGenericProperty<BlockFace>("block-face") {

    override fun readProperty(instance: BlockFace, key: String): OpenResult {
        val property: Any? = when (key) {
            "name" -> instance.name
            "direction" -> instance.direction
            "modX", "mod-x" -> instance.modX
            "modY", "mod-y" -> instance.modY
            "modZ", "mod-z" -> instance.modZ
            "oppositeFace", "opposite-face", "opposite" -> instance.oppositeFace
            "isCartesian", "is-cartesian", "cartesian" -> instance.isCartesian
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockFace, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}