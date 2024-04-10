package com.mcstarrysky.aiyatsbus.module.migrate.nereusopus

import com.mcstarrysky.aiyatsbus.core.AIYATSBUS_PREFIX
import hamsteryds.nereusopus.utils.ConfigUtils
import org.bukkit.command.CommandSender

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.migrate.nereusopus.NereusOpusMigrator
 *
 * @author mical
 * @since 2024/4/11 00:03
 */
object NereusOpusMigrator {

    fun migrate(sender: CommandSender) {
        sender.sendMessage("$AIYATSBUS_PREFIX 开始从 NereusOpus 迁移附魔数据...")
        val config = ConfigUtils.autoUpdateConfigs("", "config.yml")
        val enableUpdater = config.getBoolean("updater.enable")
        sender.sendMessage("$AIYATSBUS_PREFIX |- 是否迁移变量数据: ${!enableUpdater} (是否已开启自动平衡性调整: $enableUpdater)")
    }
}