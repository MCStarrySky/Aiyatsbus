/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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