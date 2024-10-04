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

import org.bukkit.command.CommandSender
import java.util.concurrent.CompletableFuture

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusKetherHandler
 *
 * @author mical
 * @since 2024/3/9 18:25
 */
interface AiyatsbusKetherHandler {

    /**
     * 执行 Kether 脚本
     */
    fun invoke(source: String, sender: CommandSender?, variables: Map<String, Any?> = emptyMap()): CompletableFuture<Any?>?

    /**
     * 预热 Kether 脚本, 提升第一次运行速度
     */
    fun preheat(source: String)
}