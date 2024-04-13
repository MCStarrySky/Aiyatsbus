package com.mcstarrysky.aiyatsbus.module.migrate.nereusopus.enchantments

import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import com.mcstarrysky.aiyatsbus.core.aiyatsbusEt
import com.mcstarrysky.aiyatsbus.core.data.VariableType
import com.mcstarrysky.aiyatsbus.module.migrate.nereusopus.NereusOpusMigrator
import taboolib.module.configuration.Configuration
import java.io.File

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.migrate.nereusopus.enchantments.EnchantmentTransfer
 *
 * @author mical
 * @since 2024/4/11 00:07
 */
interface EnchantmentTransfer {

    /** NereusOpus 节点名称 对 变量类型 to 节点名称 */
    val transfer: Map<String, Pair<VariableType, String>>

    val file: File

    val config: Configuration

    fun transfer(param: Boolean) {
        val enchant = aiyatsbusEt(file.nameWithoutExtension) ?: return
        enchant.config["basic.max_level"] = config["maxLevel"]
        enchant.config["basic.enable"] = config["enable"]
        enchant.config["rarity"] = NereusOpusMigrator.rarityMap[config["rarity"]] ?: AiyatsbusSettings.defaultRarity
        transfer.entries.forEach {
            val (oldNode, new) = it
            val (type, newNode) = new

            val value = config.getString("params.$oldNode")
            if (type == VariableType.LEVELED) {
                val get = enchant.config.getString("variables.leveled.$newNode")
            }
        }
    }
}