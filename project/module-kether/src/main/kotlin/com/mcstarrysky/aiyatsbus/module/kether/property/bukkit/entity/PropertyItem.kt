package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity

import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.core.util.coerceInt
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveEntity
import com.mcstarrysky.aiyatsbus.module.kether.LiveData.Companion.liveItemStack
import org.bukkit.entity.Item
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity.PropertyItem
 *
 * @author yanshiqwq
 * @since 2024/4/4 13:28
 */
@AiyatsbusProperty(
    id = "item",
    bind = Item::class
)
class PropertyItem : AiyatsbusGenericProperty<Item>("item"){
    override fun readProperty(instance: Item, key: String): OpenResult {
        val property: Any? = when (key) {
            "itemStack", "item-stack" -> instance.itemStack
            "owner" -> instance.owner
            "pickupDelay", "pickup-delay" -> instance.pickupDelay
            "thrower" -> instance.thrower
            "isUnlimitedLifetime", "is-persistent", "is-persist", "is-stable" -> instance.isUnlimitedLifetime
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: Item, key: String, value: Any?): OpenResult {
        when (key) {
            "itemStack", "item-stack" -> instance.itemStack = value?.liveItemStack ?: return OpenResult.successful()
            "owner" -> instance.owner = value?.liveEntity?.uniqueId ?: return OpenResult.successful()
            "pickupDelay", "pickup-delay" -> instance.pickupDelay = value?.coerceInt() ?: return OpenResult.successful()
            "thrower" -> instance.thrower = value?.liveEntity?.uniqueId ?: return OpenResult.successful()
            "isUnlimitedLifetime", "is-persistent", "is-persist", "is-stable" -> instance.isUnlimitedLifetime = value?.coerceBoolean() ?: return OpenResult.successful()
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}