package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.block.data

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.block.data.BlockData
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.block.data.BlockData
 *
 * @author mical
 * @since 2024/3/14 21:32
 */
@AiyatsbusProperty(
    id = "block-data",
    bind = BlockData::class
)
class PropertyBlockData : AiyatsbusGenericProperty<BlockData>("block-data") {

    override fun readProperty(instance: BlockData, key: String): OpenResult {
        val property: Any? = when (key) {
            "isOccluding", "is-occluding", "occluding" -> instance.isOccluding
            "isRandomlyTicked", "is-randomly-ticked", "randomly-ticked" -> instance.isRandomlyTicked
            "asString", "as-string" -> instance.asString
            // TODO
            else -> OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockData, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}