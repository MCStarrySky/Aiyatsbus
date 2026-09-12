package cc.polarastrum.aiyatsbus.module.compat.item

import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSource
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSources
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemType
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.manager.ItemManager
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/** NeigeItems 物品目标来源，格式：`neigeitems:<物品ID>`，例如 `neigeitems:RubySword` */
object NeigeItemsTargetItemSource : TargetItemSource {

    override val id = "neigeitems"

    override val aliases = listOf("ni")

    override fun create(identifier: String, capability: Int?, enchantability: Int): TargetItemType? {
        if (!itemSourceAvailable("NeigeItems", "pers.neige.neigeitems.manager.ItemManager")) return null
        return NeigeItemsTargetItemType(identifier, capability, enchantability)
    }

    @Awake(LifeCycle.LOAD)
    fun register() {
        TargetItemSources.register(this)
    }
}

private class NeigeItemsTargetItemType(
    identifier: String,
    capability: Int?,
    enchantability: Int
) : CustomItemTargetType(identifier, capability, enchantability) {

    override fun resolveItemId(item: ItemStack): String? = ItemManager.isNiItem(item)?.id
}
