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

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusLanguage
 *
 * @author mical
 * @since 2024/4/2 20:09
 */
interface AiyatsbusLanguage {

    /**
     * 发送语言文件
     */
    fun sendLang(sender: CommandSender, key: String, vararg args: Any)

    /**
     * 获取语言文件
     */
    fun getLang(sender: CommandSender, key: String, vararg args: Any): String

    /**
     * 获取可空语言文件
     */
    fun getLangOrNull(sender: CommandSender, key: String, vararg args: Any): String?

    /**
     * 获取语言文件
     */
    fun getLangList(sender: CommandSender, key: String, vararg args: Any): List<String>
}