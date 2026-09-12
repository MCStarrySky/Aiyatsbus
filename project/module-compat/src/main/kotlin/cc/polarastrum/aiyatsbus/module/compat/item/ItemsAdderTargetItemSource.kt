package cc.polarastrum.aiyatsbus.module.compat.item

import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSource
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSources
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemType
import dev.lone.itemsadder.api.CustomStack
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/** ItemsAdder 物品目标来源，格式：`itemsadder:<命名空间>:<物品ID>`，例如 `itemsadder:myitems:ruby_sword` */
object ItemsAdderTargetItemSource : TargetItemSource {

    override val id = "itemsadder"

    override val aliases = listOf("ia")

    override fun create(identifier: String, capability: Int?, enchantability: Int): TargetItemType? {
        if (!itemSourceAvailable("ItemsAdder", "dev.lone.itemsadder.api.CustomStack")) return null
        return ItemsAdderTargetItemType(identifier, capability, enchantability)
    }

    @Awake(LifeCycle.LOAD)
    fun register() {
        TargetItemSources.register(this)
    }
}

private class ItemsAdderTargetItemType(
    identifier: String,
    capability: Int?,
    enchantability: Int
) : CustomItemTargetType(identifier, capability, enchantability) {

    override fun resolveItemId(item: ItemStack): String? = CustomStack.byItemStack(item)?.namespacedID
}
