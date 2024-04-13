package com.mcstarrysky.aiyatsbus.module.migrate.nereusopus

import com.mcstarrysky.aiyatsbus.core.AIYATSBUS_PREFIX
import org.bukkit.command.CommandSender
import taboolib.common.io.newFile
import taboolib.common.platform.function.getDataFolder
import taboolib.module.configuration.Configuration

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.migrate.nereusopus.NereusOpusMigrator
 *
 * @author mical
 * @since 2024/4/11 00:03
 */
object NereusOpusMigrator {

    val rarityMap = mapOf(
        "common" to "普通",
        "uncommon" to "罕见",
        "rare" to "精良",
        "epic" to "史诗",
        "legendary" to "传奇",
        "unknown" to "稀世",
        "curse" to "诅咒",
        "artifact" to "皮肤"
    )

    fun migrate(sender: CommandSender) {
        sender.sendMessage("$AIYATSBUS_PREFIX 开始从 NereusOpus 迁移附魔数据...")
        val config = Configuration.loadFromFile(newFile(getDataFolder(), "../NereusOpus/config.yml"))
        val enableUpdater = config.getBoolean("updater.enable")
        sender.sendMessage("$AIYATSBUS_PREFIX |- 是否迁移变量数据: ${!enableUpdater} (是否已开启自动平衡性调整: $enableUpdater)")
    }
}