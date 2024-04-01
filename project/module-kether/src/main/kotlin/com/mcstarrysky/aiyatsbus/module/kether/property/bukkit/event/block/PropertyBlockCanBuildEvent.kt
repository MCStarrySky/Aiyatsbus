package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block

import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.block.BlockCanBuildEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.block.PropertyCanBuildEvent
 *
 * @author yanshiqwq
 * @since 2024/4/1 20:42
 */
@AiyatsbusProperty(
    id = "block-can-build-event",
    bind = BlockCanBuildEvent::class
)
class PropertyBlockCanBuildEvent : AiyatsbusGenericProperty<BlockCanBuildEvent>("block-can-build-event") {
    override fun readProperty(instance: BlockCanBuildEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "isBuildable", "buildable", "can-build" -> instance.isBuildable
            "material" -> instance.material
            "blockData", "block-data" -> instance.blockData
            "player" -> instance.player
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: BlockCanBuildEvent, key: String, value: Any?): OpenResult {
        when (key) {
            "isBuildable", "buildable", "can-build" -> instance.isBuildable = value.coerceBoolean()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}