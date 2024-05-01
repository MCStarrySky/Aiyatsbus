package com.mcstarrysky.aiyatsbus.core.data

import com.mcstarrysky.aiyatsbus.core.util.coerceInt
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
    val supports: Int = root?.getInt("supports", 11600).coerceInt(11600),
    val datapacks: List<String> = root?.getStringList("datapacks") ?: emptyList(),
    val plugins: List<String> = root?.getStringList("plugins") ?: emptyList()
) {

    fun checkAvailable(): Boolean {
        return MinecraftVersion.majorLegacy >= supports &&
                datapacks.all { pack -> Bukkit.getDatapackManager().enabledPacks.any { it.name == pack } } &&
                plugins.all { Bukkit.getPluginManager().getPlugin(it) != null }
    }
}