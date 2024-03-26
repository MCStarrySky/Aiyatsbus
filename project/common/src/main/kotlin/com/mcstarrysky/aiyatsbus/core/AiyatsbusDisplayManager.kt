package com.mcstarrysky.aiyatsbus.core

import com.mcstarrysky.aiyatsbus.core.data.Rarity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.conversion
import taboolib.platform.util.onlinePlayers

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

    @ConfigNode(bind = "enchants/display.yml")
    companion object {

        @Config("enchants/display.yml", autoReload = true)
        lateinit var conf: Configuration
            private set

        @ConfigNode("format.default_previous")
        var defaultPrevious = "{enchant_display_roman}"

        @ConfigNode("format.default_subsequent")
        var defaultSubsequent = "\n§8| §7{description}"

        @ConfigNode("capability_line")
        var capabilityLine = "§8| §7附魔词条数空余: §e{capability}"

        @ConfigNode("sort.level")
        var sortByLevel = true

        @delegate:ConfigNode("sort.rarity.order")
        val rarityOrder by conversion<List<String>, List<String>> {
            toMutableList().also { it += aiyatsbusRarities.keys.filterNot(this::contains) }
        }

        @ConfigNode("combine.enable")
        var combine = false

        @ConfigNode("combine.min")
        var minimal = 8

        @ConfigNode("combine.amount")
        var amount = 2

        @ConfigNode("combine.layout")
        var layouts = listOf<String>()

        @ConfigNode("combine.separate_special")
        var separateSpecial = true

        @delegate:ConfigNode("lore_formation")
        val loreFormation by conversion<ConfigurationSection, Map<Boolean, List<String>>> {
            mapOf(true to getStringList("has_lore"), false to getStringList("without_lore"))
        }

        @Awake(LifeCycle.ENABLE)
        fun init() {
            conf.onReload {
                onlinePlayers.forEach(Player::updateInventory)
            }
        }
    }
}