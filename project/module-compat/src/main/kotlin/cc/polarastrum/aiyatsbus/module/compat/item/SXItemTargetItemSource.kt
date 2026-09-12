package cc.polarastrum.aiyatsbus.module.compat.item

import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSource
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSources
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemType
import github.saukiya.sxitem.SXItem
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * SX-Item 物品目标来源。
 *
 * 格式：`sxitem:<物品ID>`，例如 `sxitem:RubySword`；
 * 物品 ID 后的参数仅在生成物品时有效，匹配时会被忽略。
 */
object SXItemTargetItemSource : TargetItemSource {

    override val id = "sxitem"

    override val aliases = listOf("sx", "sx-item", "si")

    override fun create(identifier: String, capability: Int?, enchantability: Int): TargetItemType? {
        if (!itemSourceAvailable("SX-Item", "github.saukiya.sxitem.SXItem")) return null
        return SXItemTargetItemType(identifier, capability, enchantability)
    }

    @Awake(LifeCycle.LOAD)
    fun register() {
        TargetItemSources.register(this)
    }
}

private class SXItemTargetItemType(
    identifier: String,
    capability: Int?,
    enchantability: Int
) : CustomItemTargetType(identifier, capability, enchantability) {

    override fun resolveItemId(item: ItemStack): String? = SXItem.getItemManager().getGenerator(item)?.key

    override fun accept(actualId: String): Boolean =
        identifier.substringBefore(':').equals(actualId, ignoreCase = true)
}
