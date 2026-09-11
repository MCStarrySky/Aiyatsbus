package cc.polarastrum.aiyatsbus.module.ingame.command.subcommand

import cc.polarastrum.aiyatsbus.core.Aiyatsbus
import cc.polarastrum.aiyatsbus.core.sendLang
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand

/**
 * Aiyatsbus
 * cc.polarastrum.aiyatsbus.module.ingame.command.subcommand.Patch
 *
 * /aiyatsbus patch <版本编号>
 * 手动应用附魔包补丁，将补丁中列出的新增文件复制到 enchants 目录，
 * 并将补丁声明的新增 event-mapping 写入服务器的 event-mapping.yml。
 *
 * @author mical
 * @since 2026/8/27
 */
val patchSubCommand = subCommand {
    dynamic("version") {
        suggestion<CommandSender> { _, _ ->
            Aiyatsbus.api().getPatchManager().getPatches().map { it.name }
        }
        execute<CommandSender> { sender, args, _ ->
            handlePatch(sender, args["version"])
        }
    }
}

private fun handlePatch(sender: CommandSender, name: String) {
    val result = Aiyatsbus.api().getPatchManager().applyPatch(name)
    val patch = result.patch ?: run {
        sender.sendLang("command-subCommands-patch-not-found", name to "name")
        return
    }

    if (result.addedFiles.isNotEmpty()) {
        sender.sendLang(
            "command-subCommands-patch-added",
            patch.name to "patch",
            result.addedFiles.size to "count",
            result.addedFiles.joinToString(", ") to "files"
        )
    }
    if (result.skippedFiles.isNotEmpty()) {
        sender.sendLang(
            "command-subCommands-patch-skipped",
            patch.name to "patch",
            result.skippedFiles.size to "count",
            result.skippedFiles.joinToString(", ") to "files"
        )
    }
    if (result.missingFiles.isNotEmpty()) {
        sender.sendLang(
            "command-subCommands-patch-missing",
            patch.name to "patch",
            result.missingFiles.size to "count",
            result.missingFiles.joinToString(", ") to "files"
        )
    }

    if (result.addedEventMappings.isNotEmpty()) {
        sender.sendLang(
            "command-subCommands-patch-event-added",
            patch.name to "patch",
            result.addedEventMappings.size to "count",
            result.addedEventMappings.joinToString(", ") to "mappings"
        )
    }
    if (result.skippedEventMappings.isNotEmpty()) {
        sender.sendLang(
            "command-subCommands-patch-event-skipped",
            patch.name to "patch",
            result.skippedEventMappings.size to "count",
            result.skippedEventMappings.joinToString(", ") to "mappings"
        )
    }
    if (result.missingEventMappings.isNotEmpty()) {
        sender.sendLang(
            "command-subCommands-patch-event-missing",
            patch.name to "patch",
            result.missingEventMappings.size to "count",
            result.missingEventMappings.joinToString(", ") to "mappings"
        )
    }

    // 完整应用且有实际新增内容（文件或事件映射）时才执行重载，让改动尽快生效。
    if (result.success && (result.addedFiles.isNotEmpty() || result.addedEventMappings.isNotEmpty())) {
        reloadPlugin(sender)
    }
}
