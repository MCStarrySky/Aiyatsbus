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

import org.bukkit.Bukkit
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.nms.MinecraftVersion

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.data.Dependencies
 *
 * @author mical
 * @since 2024/5/1 18:30
 */
class Dependencies(
    val root: ConfigurationSection?,
    supportsRangeStr: String = root?.getString("supports", "11600") ?: "11600",
    supportsLowest: Int = if ('-' in supportsRangeStr) supportsRangeStr.split('-')[0].toInt() else supportsRangeStr.toInt(),
    supportsHighest: Int = if ('-' in supportsRangeStr) supportsRangeStr.split('-')[1].toInt() else Int.MAX_VALUE,
    val datapacks: List<String> = root?.getStringList("datapacks") ?: emptyList(),
    val plugins: List<String> = root?.getStringList("plugins") ?: emptyList()
) {

    /**
     * 版本支持列表
     */
    val supportsRange = supportsLowest..supportsHighest

    fun checkAvailable(): Boolean {
        return MinecraftVersion.majorLegacy in supportsRange &&
                datapacks.all { pack -> Bukkit.getDatapackManager().enabledPacks.any { it.name == pack } } &&
                plugins.all { Bukkit.getPluginManager().getPlugin(it) != null }
    }
}