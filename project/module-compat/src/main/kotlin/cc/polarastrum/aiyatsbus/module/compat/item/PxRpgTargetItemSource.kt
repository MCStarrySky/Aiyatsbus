package cc.polarastrum.aiyatsbus.module.compat.item

import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSource
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemSources
import cc.polarastrum.aiyatsbus.core.data.registry.TargetItemType
import com.pxpmc.pxrpg.api.MAPI
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * PxRpg 物品目标来源。
 *
 * 格式：`pxrpg:<模块>:<物品ID>`，模块可以是 `item`、`equip` 或 `gem`，
 * 例如 `pxrpg:equip:RubySword`；也可以只填写物品 ID。
 */
object PxRpgTargetItemSource : TargetItemSource {

    override val id = "pxrpg"

    override val aliases = listOf("pr")

    override fun create(identifier: String, capability: Int?, enchantability: Int): TargetItemType? {
        if (!itemSourceAvailable("PxRpg", "com.pxpmc.pxrpg.api.MAPI")) return null
        return PxRpgTargetItemType(identifier, capability, enchantability)
    }

    @Awake(LifeCycle.LOAD)
    fun register() {
        TargetItemSources.register(this)
    }
}

private class PxRpgTargetItemType(
    identifier: String,
    capability: Int?,
    enchantability: Int
) : CustomItemTargetType(identifier, capability, enchantability) {

    override fun resolveItemId(item: ItemStack): String? {
        val itemStack = MAPI.getBukkitPxRpgAPI().toPxRpgItemStack(item)
        if (!itemStack.isPxRpgItem) return null
        return itemStack.toPxRpgItem()?.id
    }

    override fun accept(actualId: String): Boolean =
        identifier.substringAfter(':').substringBefore(',').equals(actualId, ignoreCase = true)
}
