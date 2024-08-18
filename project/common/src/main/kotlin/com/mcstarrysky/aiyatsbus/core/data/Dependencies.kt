package com.mcstarrysky.aiyatsbus.core.data

import org.bukkit.Bukkit
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.nms.MinecraftVersion

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.data.Dependencies
 *
 * @author mical
 * @since 2024/5/1 18:30
 */
class Dependencies(
    val root: ConfigurationSection?,
    supportsRangeStr: String = root?.getString("supports", "11600")!!,
    supportsLowest: Int = if ('-' in supportsRangeStr) supportsRangeStr.split('-')[0].toInt() else supportsRangeStr.toInt(),
    supportsHighest: Int = if ('-' in supportsRangeStr) supportsRangeStr.split('-')[1].toInt() else Int.MAX_VALUE,
    val datapacks: List<String> = root?.getStringList("datapacks") ?: emptyList(),
    val plugins: List<String> = root?.getStringList("plugins") ?: emptyList()
) {

    /**
     * 版本支持列表
     */
    val supportsRange = supportsLowest..supportsHighest

    fun checkAvailable(): Boolean {
        return MinecraftVersion.majorLegacy in supportsRange &&
                datapacks.all { pack -> Bukkit.getDatapackManager().enabledPacks.any { it.name == pack } } &&
                plugins.all { Bukkit.getPluginManager().getPlugin(it) != null }
    }
}