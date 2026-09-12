package cc.polarastrum.aiyatsbus.module.compat.item

import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSource
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSources
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemType
import io.rokuko.azureflow.api.AzureFlowAPI
import io.rokuko.azureflow.features.item.factory.AzureFlowItemFactory
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/** AzureFlow 物品目标来源，格式：`azureflow:<物品ID>`，例如 `azureflow:ruby_sword` */
object AzureFlowTargetItemSource : TargetItemSource {

    override val id = "azureflow"

    override val aliases = listOf("af")

    override fun create(identifier: String, capability: Int?, enchantability: Int): TargetItemType? {
        if (!itemSourceAvailable("AzureFlow", "io.rokuko.azureflow.api.AzureFlowAPI")) return null
        return AzureFlowTargetItemType(identifier, capability, enchantability)
    }

    @Awake(LifeCycle.LOAD)
    fun register() {
        TargetItemSources.register(this)
    }
}

private class AzureFlowTargetItemType(
    identifier: String,
    capability: Int?,
    enchantability: Int
) : CustomItemTargetType(identifier, capability, enchantability) {

    override fun resolveItemId(item: ItemStack): String? {
        val factory = AzureFlowAPI.toItem(item)?.factory as? AzureFlowItemFactory ?: return null
        return factory.getName()
    }
}
