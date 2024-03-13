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
        println(serializedData)
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
            "cooldown==${cooldown.map { (id, stamp) -> "$id=$stamp" }.joinToString { ";" }}"
}

enum class MenuMode {
    NORMAL,
    CHEAT
}
