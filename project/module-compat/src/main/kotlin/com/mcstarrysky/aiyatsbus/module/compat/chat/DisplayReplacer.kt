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
package com.mcstarrysky.aiyatsbus.module.compat.chat

import com.mcstarrysky.aiyatsbus.module.compat.chat.display.DisplayReplacerDataComponents
import com.mcstarrysky.aiyatsbus.module.compat.chat.display.DisplayReplacerNBT
import org.bukkit.entity.Player
import taboolib.common.util.unsafeLazy
import taboolib.module.nms.MinecraftVersion

/**
 * 用以替换 Component 中的 HoverEvent，使之支持更多附魔展示
 *
 * @author mical
 * @since 2024/8/18 16:36
 */
interface DisplayReplacer {

    /**
     * 替换 IChatBaseComponent 或 Component 中的 HoverEvent
     */
    fun apply(component: Any, player: Player): Any

    companion object {

        val inst by unsafeLazy {
            if (MinecraftVersion.majorLegacy >= 12005) {
                DisplayReplacerDataComponents
            } else {
                DisplayReplacerNBT
            }
        }
    }
}