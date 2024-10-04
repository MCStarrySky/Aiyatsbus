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
package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal

import com.mcstarrysky.aiyatsbus.core.asLangOrNull
import org.bukkit.command.CommandSender

enum class UIType {
    ANVIL,
    ENCHANT_INFO,
    ENCHANT_SEARCH,
    FILTER_GROUP,
    FILTER_RARITY,
    FILTER_TARGET,
    ITEM_CHECK,
    MAIN_MENU,
    FAVORITE,
    UNKNOWN;

    fun display(sender: CommandSender): String? = sender.asLangOrNull("ui-type-${name.lowercase()}")
}