package cc.polarastrum.aiyatsbus.module.compat.item

import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSource
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSources
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemType
import io.th0rgal.oraxen.api.OraxenItems
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/** Oraxen 物品目标来源，格式：`oraxen:<物品ID>`，例如 `oraxen:ruby_sword` */
object OraxenTargetItemSource : TargetItemSource {

    override val id = "oraxen"

    override val aliases = listOf("ox")

    override fun create(identifier: String, capability: Int?, enchantability: Int): TargetItemType? {
        if (!itemSourceAvailable("Oraxen", "io.th0rgal.oraxen.api.OraxenItems")) return null
        return OraxenTargetItemType(identifier, capability, enchantability)
    }

    @Awake(LifeCycle.LOAD)
    fun register() {
        TargetItemSources.register(this)
    }
}

private class OraxenTargetItemType(
    identifier: String,
    capability: Int?,
    enchantability: Int
) : CustomItemTargetType(identifier, capability, enchantability) {

    override fun resolveItemId(item: ItemStack): String? = OraxenItems.getIdByItem(item)
}
