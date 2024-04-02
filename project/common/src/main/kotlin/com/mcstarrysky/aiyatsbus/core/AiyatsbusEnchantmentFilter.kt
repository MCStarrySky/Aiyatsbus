package com.mcstarrysky.aiyatsbus.core

import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentFilter
 *
 * @author mical
 * @since 2024/2/18 12:47
 */
interface AiyatsbusEnchantmentFilter {

    fun filter(filters: Map<FilterType, Map<String, FilterStatement>>): List<AiyatsbusEnchantment>

    fun generateLore(type: FilterType, player: Player): List<String>

    fun generateLore(type: FilterType, rules: Map<String, FilterStatement>, player: Player? = null): List<String>

    fun clearFilters(player: Player)

    fun clearFilter(player: Player, type: FilterType)

    fun getStatement(player: Player, type: FilterType, value: String): FilterStatement?

    fun addFilter(player: Player, type: FilterType, value: String, state: FilterStatement)

    fun clearFilter(player: Player, type: FilterType, value: Any)

    companion object {

        val filterTypes = FilterType.values().toList()
    }
}

enum class FilterType {
    RARITY,//("品质"),
    TARGET,//("物品类别"),
    GROUP,//("类型/定位"),
    STRING;//("名字/描述")

    fun display(player: Player?): String {
        return (player ?: Bukkit.getConsoleSender()).asLang("filter-type-${name.lowercase()}")
    }
}

enum class FilterStatement {
    ON,//("&a✔"),
    OFF;//("&c✘")

    fun symbol(player: Player?): String {
        return (player ?: Bukkit.getConsoleSender()).asLang("filter-statement-symbol-${name.lowercase()}")
    }
}