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
package com.mcstarrysky.aiyatsbus.module.ingame.command

import com.mcstarrysky.aiyatsbus.core.asLang
import com.mcstarrysky.aiyatsbus.core.asLangList
import com.mcstarrysky.aiyatsbus.core.asLangOrNull
import com.mcstarrysky.aiyatsbus.core.sendLang
import com.mcstarrysky.aiyatsbus.core.util.variable
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.component.CommandBase
import taboolib.common.platform.command.component.CommandComponent
import taboolib.common.platform.command.component.CommandComponentLiteral
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.pluginVersion
import taboolib.common.util.Strings
import taboolib.module.chat.component
import taboolib.module.nms.MinecraftVersion

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.ingame.command.CommandUtils
 *
 * @author mical
 * @since 2024/10/6 01:02
 */
@Suppress("DuplicatedCode")
fun CommandComponent.createTabooLegacyHelper(commandType: String = "main", main: Boolean = commandType == "main") {
    val prefix = "command" + if (main) "" else "-$commandType"
    execute<CommandSender> { sender, _, _ ->
        val text = mutableListOf<String>()

        for (command in children.filterIsInstance<CommandComponentLiteral>()) {
            if (!sender.isOp) {
                if (!sender.hasPermission(command.permission)) continue
                else if (command.hidden) continue
            }
            val name = command.aliases[0]
            var usage = sender.asLangOrNull("$prefix-subCommands-$name-usage") ?: ""
            if (usage.isNotEmpty()) usage += " "
            val description = sender.asLangOrNull("$prefix-subCommands-$name-description") ?: sender.asLang("$prefix-no-desc")
            text += sender.asLangList("$prefix-sub", name to "name", description to "description", usage to "usage")
        }

        sender.asLangList(
            "$prefix-helper",
            pluginVersion to "pluginVersion",
            MinecraftVersion.minecraftVersion to "minecraftVersion"
        ).variable("subCommands", text).forEach { it.component().buildColored().sendTo(adaptCommandSender(sender)) }
    }

    if (this is CommandBase) {
        incorrectCommand { s, ctx, _, state ->
            val sender = s.cast<CommandSender>()
            val input = ctx.args().first()
            val name = children.filterIsInstance<CommandComponentLiteral>()
                .firstOrNull { it.aliases.contains(input) }?.aliases?.get(0) ?: input
            var usage = sender.asLangOrNull("$prefix-subCommands-$name-usage") ?: ""
            if (usage.isNotEmpty()) usage += " "
            val description = sender.asLangOrNull("$prefix-subCommands-$name-description") ?: sender.asLang("$prefix-no-desc")

            when (state) {
                // 缺参数
                1 -> {
                    sender.sendLang("$prefix-argument-missing", name to "name", usage to "usage", description to "description")
                }

                // 参数错误
                2 -> {
                    if (ctx.args().size > 1) {
                        sender.sendLang("$prefix-argument-wrong", name to "name", usage to "usage", description to "description")
                    } else {
                        val similar = children.filterIsInstance<CommandComponentLiteral>()
                            .filterNot { it.hidden }
                            .filter { sender.hasPermission(it.permission) }
                            .maxByOrNull { Strings.similarDegree(name, it.aliases[0]) }!!
                            .aliases[0]
                        sender.sendLang("$prefix-argument-unknown", name to "name", similar to "similar")
                    }
                }
            }
        }

        incorrectSender { sender, ctx ->
            (sender.cast<CommandSender>()).sendLang("$prefix-incorrect-sender", ctx.args().first() to "name")
        }
    }
}