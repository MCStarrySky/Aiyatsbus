package cc.polarastrum.aiyatsbus.module.compat.item

import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSource
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSources
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemType
import ink.ptms.zaphkiel.ZaphkielAPI
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/** Zaphkiel 物品目标来源，格式：`zaphkiel:<物品ID>`，例如 `zaphkiel:RubySword` */
object ZaphkielTargetItemSource : TargetItemSource {

    override val id = "zaphkiel"

    override val aliases = listOf("zap", "zl")

    override fun create(identifier: String, capability: Int?, enchantability: Int): TargetItemType? {
        if (!itemSourceAvailable("Zaphkiel", "ink.ptms.zaphkiel.ZaphkielAPI")) return null
        return ZaphkielTargetItemType(identifier, capability, enchantability)
    }

    @Awake(LifeCycle.LOAD)
    fun register() {
        TargetItemSources.register(this)
    }
}

private class ZaphkielTargetItemType(
    identifier: String,
    capability: Int?,
    enchantability: Int
) : CustomItemTargetType(identifier, capability, enchantability) {

    override fun resolveItemId(item: ItemStack): String? = ZaphkielAPI.getItem(item)?.id
}
