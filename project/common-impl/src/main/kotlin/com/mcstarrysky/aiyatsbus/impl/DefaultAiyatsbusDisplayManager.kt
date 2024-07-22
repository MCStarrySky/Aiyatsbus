@file:Suppress("deprecation")

package com.mcstarrysky.aiyatsbus.impl

import com.mcstarrysky.aiyatsbus.core.*
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
import taboolib.module.chat.Source
import taboolib.module.chat.component
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.conversion
import taboolib.platform.util.modifyMeta

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.DefaultAiyatsbusDisplayManager
 *
 * @author mical
 * @since 2024/2/17 22:00
 */
class DefaultAiyatsbusDisplayManager : AiyatsbusDisplayManager {

    override fun getSettings(): AiyatsbusDisplayManager.Settings {
        return AiyatsbusDisplaySettings
    }

    /**
     * 按照配置中设定的品质排行顺序整理附魔
     */
    override fun sortEnchants(enchants: Map<AiyatsbusEnchantment, Int>): LinkedHashMap<AiyatsbusEnchantment, Int> {
        return linkedMapOf(*enchants.toList().sortedBy { (enchant, level) ->
            getSettings().rarityOrder.indexOf(enchant.rarity.id) * 100000 + (if (getSettings().sortByLevel) level else 0)
        }.toTypedArray())
    }

    /**
     * 生成展示物品的 Lore
     */
    private fun generateLore(item: ItemStack? = null, player: Player? = null): List<String> {
        val settings = getSettings()

        /** 判断附魔是否需要在最后单独显示 */
        fun isSpecial(enchant: AiyatsbusEnchantment): Boolean {
            return settings.separateSpecial && !enchant.displayer.isDefaultDisplay()
        }

        // 首先确保物品必须存在
        if (item == null) return emptyList()
        // 整理附魔
        val sortedEnchants = item.fixedEnchants.ifEmpty { return emptyList() }.let(::sortEnchants)
        return buildList {
            // 如果合并模式已打开, 且达到附魔数量的最低要求
            if (settings.combine && sortedEnchants.size >= settings.combineMinimal) {
                val combineAmount = settings.combineAmount
                val combineLayout = settings.combineLayout
                // 普通附魔
                val commonEnchants = sortedEnchants.filterNot { isSpecial(it.key) }.toList()
                // 特殊附魔, 需要单独显示的
                val specialEnchants = sortedEnchants.filter { isSpecial(it.key) }.toList()
                // 按照每 combineAmount 处理数量个数据将普通附魔分割, 并分别处理
                commonEnchants.chunked(combineAmount).forEach { chunk ->
                    var layout = combineLayout[chunk.size]
                    chunk.forEachIndexed { index, (enchant, level) ->
                        layout = layout.replace(enchant.displayer.displays(level, player, item, index + 1))
                    }
                    add(layout)
                }
                // 最后再处理特殊附魔, 在末尾添加
                specialEnchants.forEach { (enchant, level) ->
                    add(combineLayout[0].replace(enchant.displayer.displays(level, player, item, 1)))
                }
            } else {
                // 没有打开附魔合并, 直接按照整理好的顺序添加附魔即可, 调用 Displayer 生成附魔的显示
                addAll(sortedEnchants.map { (enchant, level) ->
                    enchant.displayer.display(level, player, item)
                })
            }
        }.flatMap { it.split("\n") } // 处理换行
    }


    override fun display(item: ItemStack, player: Player): ItemStack {
        val settings = getSettings()
        // 如果没有打开附魔显示模块就不显示了
        if (!settings.enable) return item
        // 首先确保物品必须存在
        if (item.isNull) return item

        // 必须 **克隆** 物品, 不得修改原物品
        return item.clone().modifyMeta<ItemMeta> {
            // 如果没有任何附魔则不进行任何处理
            item.fixedEnchants.ifEmpty { return@modifyMeta }
            // 已经展示过了就不再展示 (不是重新展示)
            // NOTICE 从 0.7 版本, 我遵循白熊的嘱托, 废除了 display_mark, 以节省性能
            val loreIndex = this["lore_index", PersistentDataType.INTEGER_ARRAY]
            if (loreIndex != null && loreIndex.isNotEmpty()) {
                return@modifyMeta
            }
            // 注意, 附魔书对应的隐藏附魔 flag 是 HIDE POTION EFFECTS 而不是 HIDE ENCHANTS (1.18- 是这样, 1.19+ 未知)
            // FIXME: 1.20.5+ 更改了 HIDE_POTION_EFFECTS
            if (item.isEnchantedBook)
                if (hasItemFlag(ItemFlag.HIDE_ENCHANTS) || hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)) return@modifyMeta
                else addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS)
            else
                if (hasItemFlag(ItemFlag.HIDE_ENCHANTS)) return@modifyMeta
                else addItemFlags(ItemFlag.HIDE_ENCHANTS)
            // 记录原始 Lore 在展示后的物品里对应的索引范围
            var firstIndex = 0
            var lastIndex = 0
            // 生成附魔展示 Lore
            val generatedLore = generateLore(item, player)
            // 物品的原始 Lore
            val originLore = lore ?: emptyList()
            // 获取附魔显示格式
            val loreFormation =
                if (originLore.isNotEmpty()) settings.hasLoreFormattion else settings.withoutLoreFormattion
            // 处理最终 Lore
            val result = buildList<String> {
                loreFormation.forEach { line ->
                    when (line) {
                        "{enchant_lore}" -> addAll(generatedLore.toBuiltComponent().map(Source::toLegacyText))
                        "{capability_line}" ->
                            add(
                                settings.capabilityLine
                                    .replace("capability" to item.type.capability - item.fixedEnchants.size)
                                    .component().buildColored().toLegacyText()
                            )
                        "{item_lore}" -> {
                            // 如果是插入物品原 Lore 就在插入前后记录索引
                            firstIndex = size
                            addAll(originLore)
                            lastIndex = size
                        }
                        else -> add(line.component().buildColored().toLegacyText())
                    }
                }
            }
            // 设置显示 Lore
            lore = result
            this["lore_index", PersistentDataType.INTEGER_ARRAY] = intArrayOf(firstIndex, lastIndex)
            // 加附魔序列化数据
            // TODO: 尝试减少字符串的拼接与分割操作
            this["enchants_serialized", PersistentDataType.STRING] =
                item.fixedEnchants.map { (enchant, level) -> "${enchant.basicData.id}:$level" }.joinToString("|")
        }
    }

    override fun undisplay(item: ItemStack, player: Player): ItemStack {
        val settings = getSettings()
        // 如果没开启就不处理
        if (!settings.enable) return item
        // 物品为空就不处理
        if (item.isNull) return item
        return item.clone().modifyMeta<ItemMeta> {
            // 判断是不是没有处理过
            val loreIndex = this["lore_index", PersistentDataType.INTEGER_ARRAY]
            if (loreIndex == null || loreIndex.isEmpty()) {
                return@modifyMeta
            }
            removeItemFlags(ItemFlag.HIDE_ENCHANTS)
            if (item.isEnchantedBook) removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS)

            // 创造模式的额外处理，需要重新给物品附魔
            // 这是因为创造模式下客户端会重新设置背包物品，而重新设置的物品中非原版附魔会消失
            // 这是由于客户端没有注册更多附魔
            // FIXME: 不知道 1.21 是不是不需要这步啊, 到时候测试一下
            if (player.gameMode == GameMode.CREATIVE) {
                this["enchants_serialized", PersistentDataType.STRING]!!.split("|").forEach { pair ->
                    aiyatsbusEt(pair.split(":")[0])?.let { enchant ->
                        addEt(enchant, pair.split(":")[1].toInt())
                    }
                }
            }
            // 除去 enchant lore
            val (first, last) = loreIndex
            lore = lore!!.subList(first, last)
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

    @ConfigNode(bind = "enchants/display.yml")
    object AiyatsbusDisplaySettings : AiyatsbusDisplayManager.Settings {

        @Config("enchants/display.yml", autoReload = true)
        override lateinit var conf: Configuration

        @ConfigNode("enable")
        override var enable = true

        @ConfigNode("format.default_previous")
        override var defaultPrevious = "{enchant_display_roman}"

        @ConfigNode("format.default_subsequent")
        override var defaultSubsequent = "\n&8| &7{description}"

        @ConfigNode("capability_line")
        override var capabilityLine = "&8| &7附魔词条数空余: &e{capability}"

        @ConfigNode("sort.level")
        override var sortByLevel = true

        @delegate:ConfigNode("sort.rarity.order")
        override val rarityOrder by conversion<List<String>, List<String>> {
            toMutableList().also { it += aiyatsbusRarities.keys.filterNot(this::contains) }
        }

        @ConfigNode("combine.enable")
        override var combine = false

        @ConfigNode("combine.min")
        override var combineMinimal = 8

        @ConfigNode("combine.amount")
        override var combineAmount = 2

        @ConfigNode("combine.layout")
        override var combineLayout = listOf<String>()

        @ConfigNode("combine.separate_special")
        override var separateSpecial = true

        @ConfigNode("lore_formattion.has_lore")
        override var hasLoreFormattion = listOf<String>()

        @ConfigNode("lore_formattion.without_lore")
        override var withoutLoreFormattion = listOf<String>()
    }
}