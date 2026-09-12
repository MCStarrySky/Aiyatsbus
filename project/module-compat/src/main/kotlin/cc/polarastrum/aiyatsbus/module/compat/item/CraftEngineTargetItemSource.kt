package cc.polarastrum.aiyatsbus.module.compat.item

import cc.polarastrum.aiyatsbus.core.AiyatsbusSettings
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSource
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSources
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemType
import cc.polarastrum.aiyatsbus.core.util.craftEngineEnabled
import net.momirealms.craftengine.bukkit.api.CraftEngineItems
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/** CraftEngine 物品目标来源，格式：`craftengine:<物品ID>`，例如 `craftengine:weapons:diamond_sword` */
object CraftEngineTargetItemSource : TargetItemSource {

    override val id = "craftengine"

    override val aliases = listOf("ce")

    override fun create(identifier: String, capability: Int?, enchantability: Int): TargetItemType? {
        if (!AiyatsbusSettings.supportCraftEngine || !craftEngineEnabled) return null
        return CraftEngineTargetItemType(identifier, capability, enchantability)
    }

    @Awake(LifeCycle.LOAD)
    fun register() {
        TargetItemSources.register(this)
    }
}

private class CraftEngineTargetItemType(
    identifier: String,
    capability: Int?,
    enchantability: Int
) : CustomItemTargetType(identifier, capability, enchantability) {

    override fun resolveItemId(item: ItemStack): String? = CraftEngineItems.getCustomItemId(item)?.toString()
}
