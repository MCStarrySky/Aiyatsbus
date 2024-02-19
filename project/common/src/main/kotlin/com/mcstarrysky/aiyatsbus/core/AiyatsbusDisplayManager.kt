package com.mcstarrysky.aiyatsbus.core

import com.mcstarrysky.aiyatsbus.core.data.Rarity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusDisplayManager
 *
 * @author mical
 * @since 2024/2/17 21:59
 */
interface AiyatsbusDisplayManager {

    /**
     * 展示附魔, 展示是给玩家看的, 玩家必须存在
     */
    fun display(item: ItemStack, player: Player): ItemStack

    /**
     * 取消展示
     */
    fun undisplay(item: ItemStack, player: Player): ItemStack

    companion object {

        @Config("enchants/display.yml", autoReload = true)
        lateinit var conf: Configuration
            private set

        @ConfigNode("format.default_previous", bind = "enchants/display.yml")
        var defaultPrevious = "{enchant_display_roman}"

        @ConfigNode("format.default_subsequent", bind = "enchants/display.yml")
        var defaultSubsequent = "\n§8| §7{description}"

        @ConfigNode("capability_line", bind = "enchants/display.yml")
        var capabilityLine = "§8| §7附魔词条数空余: §e{capability}"

        @ConfigNode("sort.level", bind = "enchants/display.yml")
        var sortByLevel = true

        var rarityOrder = listOf<String>()

        @ConfigNode("combine.enable", bind = "enchants/display.yml")
        var combine = false

        @ConfigNode("combine.min", bind = "enchants/display.yml")
        var minimal = 8

        @ConfigNode("combine.amount", bind = "enchants/display.yml")
        var amount = 2

        @ConfigNode("combine.layout", bind = "enchants/display.yml")
        var layouts = listOf<String>()

        @ConfigNode("combine.separate_special", bind = "enchants/display.yml")
        var separateSpecial = true

        var loreFormation = mutableMapOf<Boolean, List<String>>()

        @Awake(LifeCycle.ENABLE)
        fun reload() {
            val order = conf.getStringList("sort.rarity.order")
            rarityOrder = order + Rarity.rarities.keys.filterNot(order::contains)

            loreFormation[true] = conf.getStringList("lore_formation.has_lore")
            loreFormation[false] = conf.getStringList("lore_formation.without_lore")
        }

        @Awake(LifeCycle.ENABLE)
        fun autoReload() {
            conf.onReload { reload() }
        }
    }
}