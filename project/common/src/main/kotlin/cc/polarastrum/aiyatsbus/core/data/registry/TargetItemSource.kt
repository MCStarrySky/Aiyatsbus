package cc.polarastrum.aiyatsbus.core.data.registry

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/** 目标使用的已编译物品匹配器。实现应当保证事件热路径上的调用开销足够低。 */
interface TargetItemSource {
    val id: String

    /**
     * 物品源的别名，便于按习惯书写，例如 `itemsadder` 的 `ia`。
     *
     * 别名不会覆盖已经注册的物品源 ID。
     */
    val aliases: List<String> get() = emptyList()

    fun create(identifier: String, capability: Int?, enchantability: Int): TargetItemType?
}

interface TargetItemType {
    val identifier: String
    val capability: Int?
    val enchantability: Int
    val hasEnchantability: Boolean
    val vanillaMaterial: Material?

    fun matches(item: ItemStack): Boolean
}

object TargetItemSources {
    private val sources = linkedMapOf<String, TargetItemSource>()

    init {
        register(VanillaTargetItemSource)
    }

    fun register(source: TargetItemSource) {
        sources[source.id.lowercase()] = source
        source.aliases.forEach { sources.putIfAbsent(it.lowercase(), source) }
    }

    fun create(identifier: String, capability: Int?, enchantability: Int): TargetItemType? {
        val separator = identifier.indexOf(':')
        val sourceId = if (separator == -1) "minecraft" else identifier.substring(0, separator)
        val sourceIdentifier = if (separator == -1) identifier else identifier.substring(separator + 1)
        return sources[sourceId.lowercase()]?.create(sourceIdentifier, capability, enchantability)
    }
}

private object VanillaTargetItemSource : TargetItemSource {
    override val id = "minecraft"

    override fun create(identifier: String, capability: Int?, enchantability: Int): TargetItemType? {
        val material = runCatching { Material.matchMaterial(identifier) }.getOrNull() ?: return null
        return object : TargetItemType {
            override val identifier = material.name
            override val capability = capability
            override val enchantability = enchantability
            override val hasEnchantability = enchantability > 0
            override val vanillaMaterial = material
            override fun matches(item: ItemStack) = item.type == material
        }
    }
}
