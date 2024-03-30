package com.mcstarrysky.aiyatsbus.module.ingame.command

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.util.Reloadable
import com.mcstarrysky.aiyatsbus.module.ingame.command.subcommand.*
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.variable
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.common.platform.command.component.CommandBase
import taboolib.common.platform.command.component.CommandComponent
import taboolib.common.platform.command.component.CommandComponentLiteral
import taboolib.common.platform.function.pluginVersion
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.common.util.Strings
import taboolib.module.chat.component
import taboolib.module.lang.asLangText
import taboolib.module.lang.asLangTextList
import taboolib.module.lang.asLangTextOrNull
import taboolib.module.lang.sendLang
import taboolib.module.nms.MinecraftVersion

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.command.AiyatsbusCommand
 *
 * @author mical
 * @since 2024/2/17 21:02
 */
@CommandHeader(name = "aiyatsbus", permission = "aiyatsbus.command", aliases = ["se", "spe", "splendidenchants", "nereus", "nos", "nereusopus"])
object AiyatsbusCommand {

    val enchantNamesAndIds = mutableListOf<String>()

    @CommandBody(permission = "aiyatsbus.command")
    val main = mainCommand {
        createTabooLegacyHelper()
    }

    @CommandBody(permission = "aiyatsbus.command.book")
    val book = bookSubCommand

    @CommandBody(permission = "aiyatsbus.command.enchant")
    val enchant = enchantSubCommand

    @CommandBody(permission = "aiyatsbusm.command.menu")
    val menu = menuSubCommand

    @CommandBody(permission = "aiyatsbus.command.mode")
    val mode = modeSubCommand

    @CommandBody(permission = "aiyatsbus.command.random")
    val random = randomSubCommand

    @CommandBody(permission = "aiyatsbus.command.reload")
    val reload = reloadSubCommand

    @CommandBody(permission = "aiyatsbus.command.report")
    val report = reportSubCommand

    @Reloadable
    @Awake(LifeCycle.INIT)
    fun init() {
        registerLifeCycleTask(LifeCycle.ENABLE, 999) {
            // 生成 TabList
            enchantNamesAndIds.clear()
            enchantNamesAndIds.addAll(Aiyatsbus.api().getEnchantmentManager().getByIDs().keys + Aiyatsbus.api().getEnchantmentManager().getByNames().keys)
        }
    }
}

fun CommandComponent.createTabooLegacyHelper() {
    execute<ProxyCommandSender> { sender, _, _ ->
        val text = mutableListOf<String>()

        for (command in children.filterIsInstance<CommandComponentLiteral>()) {
            if (!sender.isOp) {
                if (!sender.hasPermission(command.permission)) continue
                else if (command.hidden) continue
            }
            val name = command.aliases[0]
            var usage = sender.asLangTextOrNull("command-subCommands-$name-usage") ?: ""
            if (usage.isNotEmpty()) usage += " "
            val description = sender.asLangTextOrNull("command-subCommands-$name-description") ?: sender.asLangText("command-no-desc")
            text += sender.asLangTextList("command-sub", name to "name", description to "description", usage to "usage")
        }

        sender.asLangTextList(
            "command-helper",
            pluginVersion to "pluginVersion",
            MinecraftVersion.minecraftVersion to "minecraftVersion"
        ).variable("subCommands", text).forEach { it.component().buildColored().sendTo(sender) }
    }

    if (this is CommandBase) {
        incorrectCommand { sender, ctx, _, state ->
            val input = ctx.args().first()
            val name = children.filterIsInstance<CommandComponentLiteral>()
                .firstOrNull { it.aliases.contains(input) }?.aliases?.get(0) ?: input
            var usage = sender.asLangTextOrNull("command-subCommands-$name-usage") ?: ""
            if (usage.isNotEmpty()) usage += " "
            val description = sender.asLangTextOrNull("command-subCommands-$name-description") ?: sender.asLangText("command-no-desc")

            when (state) {
                // 缺参数
                1 -> {
                    sender.sendLang("command-argument-missing", name to "name", usage to "usage", description to "description")
                }

                // 参数错误
                2 -> {
                    if (ctx.args().size > 1) {
                        sender.sendLang("command-argument-wrong", name to "name", usage to "usage", description to "description")
                    } else {
                        val similar = children.filterIsInstance<CommandComponentLiteral>()
                            .filterNot { it.hidden }
                            .filter { sender.hasPermission(it.permission) }
                            .maxByOrNull { Strings.similarDegree(name, it.aliases[0]) }!!
                            .aliases[0]
                        sender.sendLang("command-argument-unknown", name to "name", similar to "similar")
                    }
                }
            }
        }

        incorrectSender { sender, ctx ->
            sender.sendLang("command-incorrect-sender", ctx.args().first() to "name")
        }
    }
}