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

import taboolib.library.configuration.ConfigurationSection

/**
 * 附魔的基本数据, 必填的
 *
 * @author mical
 * @since 2024/2/17 14:37
 */
data class BasicData(
    private val root: ConfigurationSection,
    val enable: Boolean = root.getBoolean("enable", true),
    val disableWorlds: List<String> = root.getStringList("disable_worlds"),
    val id: String = root.getString("id")!!,
    val name: String = root.getString("name")!!,
    val maxLevel: Int = root.getInt("max_level", 1)
)