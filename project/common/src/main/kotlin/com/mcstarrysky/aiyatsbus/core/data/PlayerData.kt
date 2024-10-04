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
package com.mcstarrysky.aiyatsbus.core.data

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentFilter
import com.mcstarrysky.aiyatsbus.core.FilterStatement
import com.mcstarrysky.aiyatsbus.core.FilterType
import com.mcstarrysky.aiyatsbus.core.aiyatsbusEt
import taboolib.common5.clong

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.data.PlayerData
 *
 * @author mical
 * @since 2024/2/18 12:59
 */
data class PlayerData(private val serializedData: String?) {
    var menuMode: MenuMode = MenuMode.NORMAL
    var favorites: MutableList<String> = mutableListOf()
    var filters: Map<FilterType, MutableMap<String, FilterStatement>> =
        FilterType.values().associateWith { mutableMapOf() }
    var cooldown: MutableMap<String, Long> = mutableMapOf()

    init {
        serializedData?.let {
            serializedData.split("||")
                .map { pair -> pair.split("==")[0] to pair.split("==")[1] }
                .forEach { (key, value) ->
                    when (key) {
                        "menu_mode" -> menuMode = MenuMode.valueOf(value)
                        "favorites" -> favorites.addAll(value.split(";").mapNotNull { id -> aiyatsbusEt(id)?.basicData?.id })

                        "filters" -> {
                            var tot = 0
                            value.split("$").forEach { content ->
                                filters[AiyatsbusEnchantmentFilter.filterTypes[tot++]]!!.putAll(content.split(";")
                                    .filter { filter -> filter.isNotBlank() }
                                    .associate { filter ->
                                        filter.split("=")[0] to
                                                FilterStatement.valueOf(filter.split("=")[1])
                                    })
                            }
                        }

                        "cooldown" -> {
                            cooldown.putAll(value
                                .split(";")
                                .filter { pair -> pair.isNotBlank() }
                                .associate { pair -> pair.split("=")[0] to pair.split("=")[1].clong })
                        }

                        else -> {}
                    }
                }
        }
    }

    fun serialize() = "menu_mode==$menuMode||" +
            "favorites==${favorites.joinToString(";")}||" +
            "filters==${
                AiyatsbusEnchantmentFilter.filterTypes.map {
                    filters[it]!!.map { (value, state) -> "$value=$state" }.joinToString(";")
                }.joinToString("$")
            }||" +
            "cooldown==${cooldown.map { (id, stamp) -> "$id=$stamp" }.joinToString(";")}"
}

enum class MenuMode {
    NORMAL,
    CHEAT
}
