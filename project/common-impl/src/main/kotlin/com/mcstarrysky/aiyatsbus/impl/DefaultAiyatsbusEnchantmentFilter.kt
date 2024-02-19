package com.mcstarrysky.aiyatsbus.impl

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.FilterType.*
import com.mcstarrysky.aiyatsbus.core.data.Group
import com.mcstarrysky.aiyatsbus.core.data.Rarity
import com.mcstarrysky.aiyatsbus.core.data.Target
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.DefaultAiyatsbusEnchantmentFilter
 *
 * @author mical
 * @since 2024/2/18 14:37
 */
class DefaultAiyatsbusEnchantmentFilter : AiyatsbusEnchantmentFilter {

    override fun filter(filters: Map<FilterType, Map<String, FilterStatement>>): List<AiyatsbusEnchantment> {
        return Aiyatsbus.api().getEnchantmentManager().getByIDs().values.filter result@{ enchant ->
            filters.forEach { (type, rules) ->
                var onFlag = false
                var offFlag = false
                val onExists = rules.any { it.value == FilterStatement.ON }

                rules.forEach { (value, state) ->
                    if (when (type) {
                            RARITY -> Rarity.getRarity(value) == enchant.rarity
                            TARGET -> (enchant.targets.contains(Target.targets[value]) || enchant.targets.any { Target.targets[value]?.itemTypes?.containsAll(it.itemTypes) == true })
                            GROUP -> enchant.enchantment.isInGroup(Group.groups[value])
                            STRING -> {
                                enchant.basicData.name.contains(value) ||
                                        enchant.basicData.id.contains(value) ||
                                        enchant.displayer.generalDescription.contains(value)
                            }
                        }
                    ) {
                        when (state) {
                            FilterStatement.ON -> onFlag = true
                            FilterStatement.OFF -> offFlag = true
                        }
                    }
                }
                if (offFlag) return@result false
                if (!onFlag && onExists) return@result false
            }
            true
        }
    }

    override fun generateLore(type: FilterType, player: Player): List<String> {
        return generateLore(type, player.filters[type]!!)
    }

    override fun generateLore(type: FilterType, rules: Map<String, FilterStatement>, player: Player?): List<String> {
        return rules.map { (value, state) ->
            state.symbol(player) + " " + when (type) {
                RARITY -> Rarity.getRarity(value)?.display() ?: value
                TARGET -> Target.targets[value]?.name ?: value
                GROUP -> Group.groups[value]?.name ?: value
                STRING -> value
            }
        }
    }

    override fun clearFilters(player: Player) {
        AiyatsbusEnchantmentFilter.filterTypes.forEach {
            player.filters[it]!!.clear()
        }
    }

    override fun clearFilter(player: Player, type: FilterType) {
        player.filters[type]!!.clear()
    }

    override fun clearFilter(player: Player, type: FilterType, value: Any) {
        player.filters[type]!!.remove(value)
    }

    override fun getStatement(player: Player, type: FilterType, value: String): FilterStatement? {
        return player.filters[type]!![value]
    }

    override fun addFilter(player: Player, type: FilterType, value: String, state: FilterStatement) {
        player.filters[type]!![value] = state
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusEnchantmentFilter>(DefaultAiyatsbusEnchantmentFilter())
        }
    }
}