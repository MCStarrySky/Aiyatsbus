/*
 * This file is part of Adyeshach, licensed under the MIT License.
 *
 *  Copyright (c) 2020 TabooLib
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.mcstarrysky.aiyatsbus.module.ingame.command

import com.mcstarrysky.aiyatsbus.core.ScriptWorkspace
import com.mcstarrysky.aiyatsbus.core.sendLang
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.getOpenContainers
import taboolib.module.kether.Kether
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptContext
import taboolib.module.kether.printKetherErrorMessage

@CommandHeader(name = "aiyatsbusscript", aliases = ["aiscript"], permission = "aiyatsbus.command")
object CommandScript {

    val workspace by lazy { ScriptWorkspace.workspace }

    @CommandBody
    val main = mainCommand {
        createTabooLegacyHelper("script")
    }

    @CommandBody
    val run = subCommand {
        // script
        dynamic(comment = "file") {
            suggestion<CommandSender> { _, _ ->
                workspace.scripts.map { it.value.id }
            }
            // viewer
            dynamic(comment = "viewer", optional = true) {
                suggestion<CommandSender> { _, _ ->
                    Bukkit.getOnlinePlayers().map { it.name }
                }
                // ver
                dynamic(comment = "args", optional = true) {
                    execute<CommandSender> { sender, context, argument ->
                        commandRun(sender, context.argument(-2), context.argument(-1), argument.split(" ").toTypedArray())
                    }
                }
                execute<CommandSender> { sender, context, argument ->
                    commandRun(sender, context.argument(-1), argument)
                }
            }
            execute<CommandSender> { sender, _, argument ->
                commandRun(sender, argument)
            }
        }
    }

    @CommandBody
    val stop = subCommand {
        dynamic(comment = "file", optional = true) {
            suggestion<CommandSender> { _, _ ->
                workspace.scripts.map { it.value.id }
            }
            execute<CommandSender> { sender, _, argument ->
                if (!sender.isOp) {
                    sender.sendMessage("§c§l[Aiyatsbus] §7You do not have permission.")
                }
                val script = workspace.getRunningScript().filter { it.quest.id == argument }
                if (script.isNotEmpty()) {
                    script.forEach { workspace.terminateScript(it) }
                } else {
                    sender.sendLang("command-script-not-running")
                }
            }
        }
        execute<CommandSender> { sender, _, _ ->
            if (!sender.isOp) {
                sender.sendMessage("§c§l[Aiyatsbus] §7You do not have permission.")
            }
            workspace.getRunningScript().forEach { workspace.terminateScript(it) }
        }
    }

    @CommandBody
    val list = subCommand {
        execute<CommandSender> { sender, _, _ ->
            if (!sender.isOp) {
                sender.sendMessage("§c§l[Aiyatsbus] §7You do not have permission.")
            }
            sender.sendLang("command-script-list-all",
                workspace.scripts.map { it.value.id }.joinToString(", "),
                workspace.getRunningScript().joinToString(", ") { it.id }
            )
        }
    }

    @CommandBody
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            if (!sender.isOp) {
                sender.sendMessage("§c§l[Aiyatsbus] §7You do not have permission.")
            }
            workspace.cancelAll()
            workspace.loadAll()
            sender.sendLang("command-script-reload-all")
        }
    }

    @CommandBody
    val debug = subCommand {
        execute<CommandSender> { sender, _, _ ->
            if (!sender.isOp) {
                sender.sendMessage("§c§l[Aiyatsbus] §7You do not have permission.")
            }
            sender.sendMessage(" §5§l‹ ›§f §7RegisteredActions:")
            Kether.scriptRegistry.registeredNamespace.forEach {
                sender.sendMessage(" §5§l‹ ›§f §7  ${it}: §f${Kether.scriptRegistry.getRegisteredActions(it)}")
            }
        }
    }

    @CommandBody
    val containers = subCommand {
        execute<CommandSender> { sender, _, _ ->
            if (!sender.isOp) {
                sender.sendMessage("§c§l[Aiyatsbus] §7You do not have permission.")
            }
            getOpenContainers().forEach { it ->
                sender.sendMessage(" §5§l‹ ›§f §7${it.name} §8${it.javaClass.simpleName}")
                it.call("test", emptyArray());
            }
        }
    }

    @CommandBody
    val invoke = subCommand {
        dynamic(comment = "script") {
            execute<CommandSender> { sender, _, argument ->
                if (!sender.isOp) {
                    sender.sendMessage("§c§l[Aiyatsbus] §7You do not have permission.")
                }
                try {
                    KetherShell.eval(
                        argument,
                        namespace = listOf("aiyatsbus"),
                        sender = adaptCommandSender(sender),
                        vars = KetherShell.VariableMap("player" to sender, "item" to (sender as? Player)?.equipment?.itemInMainHand)
                    ).thenApply { v ->
                        try {
                            Class.forName(v.toString().substringBefore('$'))
                            sender.sendMessage(" §5§l‹ ›§f §7Result: §f${v!!.javaClass.simpleName} §7(Java Object)")
                        } catch (_: Throwable) {
                            sender.sendMessage(" §5§l‹ ›§f §7Result: §f$v")
                        }
                    }
                } catch (ex: Throwable) {
                    sender.sendMessage(" §5§l‹ ›§f §7Error: ${ex.message}")
                    ex.printKetherErrorMessage()
                }
            }
        }
    }

    fun commandRun(sender: CommandSender, file: String, viewer: String? = null, args: Array<String> = emptyArray()) {
        if (!sender.isOp) {
            sender.sendMessage("§c§l[Aiyatsbus] §7You do not have permission.")
        }
        val script = workspace.scripts[file]
        if (script != null) {
            val context = ScriptContext.create(script) {
                if (viewer != null) {
                    val player = Bukkit.getPlayerExact(viewer)
                    if (player != null) {
                        this.sender = adaptCommandSender(player)
                    }
                }
                var i = 0
                while (i < args.size) {
                    rootFrame().variables().set("arg${i}", args[i])
                    i++
                }
            }
            try {
                workspace.runScript(file, context)
            } catch (t: Throwable) {
                sender.sendLang("command-script-error", t.localizedMessage)
                t.printKetherErrorMessage()
            }
        } else {
            sender.sendLang("command-script-not-found")
        }
    }
}