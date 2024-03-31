@file:Suppress("deprecation")

package com.mcstarrysky.aiyatsbus.impl

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.AiyatsbusDisplayManager.Companion.amount
import com.mcstarrysky.aiyatsbus.core.AiyatsbusDisplayManager.Companion.capabilityLine
import com.mcstarrysky.aiyatsbus.core.AiyatsbusDisplayManager.Companion.combine
import com.mcstarrysky.aiyatsbus.core.AiyatsbusDisplayManager.Companion.layouts
import com.mcstarrysky.aiyatsbus.core.AiyatsbusDisplayManager.Companion.loreFormation
import com.mcstarrysky.aiyatsbus.core.AiyatsbusDisplayManager.Companion.minimal
import com.mcstarrysky.aiyatsbus.core.AiyatsbusDisplayManager.Companion.rarityOrder
import com.mcstarrysky.aiyatsbus.core.AiyatsbusDisplayManager.Companion.separateSpecial
import com.mcstarrysky.aiyatsbus.core.AiyatsbusDisplayManager.Companion.sortByLevel
import com.mcstarrysky.aiyatsbus.core.util.*
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common5.cbool
import taboolib.module.chat.Source
import taboolib.module.chat.component
import taboolib.platform.util.modifyMeta
import kotlin.math.min

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.DefaultAiyatsbusDisplayManager
 *
 * @author mical
 * @since 2024/2/17 22:00
 */
class DefaultAiyatsbusDisplayManager : AiyatsbusDisplayManager {

    /**
     * 按照配置中设定的品质排行顺序整理附魔
     */
    private fun sortEnchants(enchants: Map<AiyatsbusEnchantment, Int>): LinkedHashMap<AiyatsbusEnchantment, Int> {
        return linkedMapOf(*enchants.toList().sortedBy { (enchant, level) ->
            rarityOrder.indexOf(enchant.rarity.id) * 100000 + (if (sortByLevel) level else 0)
        }.toTypedArray())
    }

    /**
     * 生成展示物品的 Lore
     */
    private fun generateLore(item: ItemStack? = null, player: Player? = null): List<String> {
        item ?: return listOf()

        val enchants = sortEnchants(item.fixedEnchants).ifEmpty { return listOf() }
        val lore = mutableListOf<String>()
        val combineMode = combine && enchants.size >= minimal

        if (!combineMode) lore += enchants.map { (enchant, level) -> enchant.displayer.display(level, player, item) }
        else {
            val enchantPairs = enchants.filter { !separateSpecial || it.key.displayer.isDefaultDisplay() }.toList()
            for (i in enchantPairs.indices step amount) {
                val total = min(amount, enchantPairs.size - i)
                var layout = layouts[total - 1]
                for (j in 0 until total) {
                    val enchantPair = enchantPairs[i + j]
                    layout = layout.replace(enchantPair.first.displayer.displays(enchantPair.second, player, item, j + 1))
                }
                lore += layout.split("\n")
            }
            enchants.filter { (et, _) -> enchantPairs.none { et == it.first } }.forEach { (enchant, level) ->
                lore += enchant.displayer.display(level, player, item);
            }
        }

        return lore.flatMap { it.split("\n") }
    }

    override fun display(item: ItemStack, player: Player): ItemStack {
        if (item.isNull) return item
        return item.clone().modifyMeta<ItemMeta> {
            item.fixedEnchants.ifEmpty { return@modifyMeta }

            // 已经展示过了就重新展示（2.0遗留思路）（白熊认为多余，白熊建议可以尝试去除此处，节省性能）
            this["display_mark", PersistentDataType.STRING]?.cbool?.let { return@modifyMeta }

            // 若本来就不需要显示附魔，就不显示了
            // 注意, 附魔书对应的隐藏附魔 flag 是 HIDE POTION EFFECTS 而不是 HIDE ENCHANTS (1.18- 是这样, 1.19+ 未知)
            if (item.isEnchantedBook)
                if (hasItemFlag(ItemFlag.HIDE_ENCHANTS) || hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)) return@modifyMeta
                else addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS)
            else
                if (hasItemFlag(ItemFlag.HIDE_ENCHANTS)) return@modifyMeta
                else addItemFlags(ItemFlag.HIDE_ENCHANTS)

            // 上标记
            this["display_mark", PersistentDataType.STRING] = "true"

            // 上lore
            val enchantLore = generateLore(item, player).toMutableList()
            val originLore = lore ?: emptyList()
            val resultLore = mutableListOf<String>()
            var first = 0
            var last = 0
            loreFormation[originLore.isNotEmpty()]!!.forEach {
                when (it) {
                    "{enchant_lore}" -> resultLore += enchantLore.toBuiltComponent().map(Source::toLegacyText)
                    "{capability_line}" -> resultLore += capabilityLine.replace("capability" to item.type.capability - item.fixedEnchants.size).component().buildColored().toLegacyText()
                    "{item_lore}" -> {
                        first = resultLore.size
                        resultLore += originLore
                        last = resultLore.size
                    }

                    else -> resultLore += it.component().buildColored().toLegacyText()
                }
            }
            lore = resultLore
            this["lore_index", PersistentDataType.STRING] = "$first-$last"
            // 加附魔序列化数据
            this["enchants_serialized", PersistentDataType.STRING] =
                item.fixedEnchants.map { (enchant, level) -> "${enchant.basicData.id}:$level" }.joinToString("|")
        }
    }

    override fun undisplay(item: ItemStack, player: Player): ItemStack {
        if (item.isNull) return item
        return item.clone().modifyMeta<ItemMeta> {
            this["display_mark", PersistentDataType.STRING]?.cbool ?: return@modifyMeta

            removeItemFlags(ItemFlag.HIDE_ENCHANTS)
            if (item.isEnchantedBook) removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS)

            // 创造模式的额外处理，需要重新给物品附魔
            // 这是因为创造模式下客户端会重新设置背包物品，而重新设置的物品中非原版附魔会消失
            // 这是由于客户端没有注册更多附魔
            if (player.gameMode == GameMode.CREATIVE) {
                this["enchants_serialized", PersistentDataType.STRING]!!.split("|").forEach { pair ->
                    aiyatsbusEt(pair.split(":")[0])?.let { enchant ->
                        addEt(enchant, pair.split(":")[1].toInt())
                    }
                }
            }

            // 除去 enchant lore
            val index = this["lore_index", PersistentDataType.STRING]!!
            val first = index.split("-")[0].toInt()
            val last = index.split("-")[1].toInt()
            lore = lore!!.subList(first, last)

            remove("display_mark")
            remove("enchants_serialized")
            remove("lore_index")
        }
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusDisplayManager>(DefaultAiyatsbusDisplayManager())
        }
    }
}