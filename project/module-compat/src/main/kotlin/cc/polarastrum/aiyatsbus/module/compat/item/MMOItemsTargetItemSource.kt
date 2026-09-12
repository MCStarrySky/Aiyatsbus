package cc.polarastrum.aiyatsbus.module.compat.item

import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSource
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSources
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemType
import net.Indyuce.mmoitems.MMOItems
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * MMOItems 物品目标来源。
 *
 * 格式：`mmoitems:<物品类型>,<物品ID>`，例如 `mmoitems:SWORD,RubySword`；
 * 也可以只填写物品 ID，此时不校验物品类型。
 */
object MMOItemsTargetItemSource : TargetItemSource {

    override val id = "mmoitems"

    override val aliases = listOf("mi", "mmo")

    override fun create(identifier: String, capability: Int?, enchantability: Int): TargetItemType? {
        if (!itemSourceAvailable("MMOItems", "net.Indyuce.mmoitems.MMOItems")) return null
        return MMOItemsTargetItemType(identifier, capability, enchantability)
    }

    @Awake(LifeCycle.LOAD)
    fun register() {
        TargetItemSources.register(this)
    }
}

private class MMOItemsTargetItemType(
    identifier: String,
    capability: Int?,
    enchantability: Int
) : CustomItemTargetType(identifier, capability, enchantability) {

    override fun resolveItemId(item: ItemStack): String? {
        val type = MMOItems.getTypeName(item) ?: return null
        val id = MMOItems.getID(item) ?: return null
        return "$type,$id"
    }

    override fun accept(actualId: String): Boolean {
        val separator = identifier.indexOf(',')
        return if (separator == -1) identifier.equals(actualId.substringAfter(','), ignoreCase = true)
        else identifier.equals(actualId, ignoreCase = true)
    }
}
