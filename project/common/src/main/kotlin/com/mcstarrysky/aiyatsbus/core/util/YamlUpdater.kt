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
package com.mcstarrysky.aiyatsbus.core.util

import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.io.newFile
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration

/**
 * module-starrysky
 * com.mcstarrysky.starrysky.config.YamlUpdater
 *
 * @author 米擦亮
 * @since 2023/9/6 20:50
 */
object YamlUpdater {

    fun loadFromFile(path: String, autoUpdate: Boolean = true, updateNodes: List<String> = emptyList(), cache: Configuration? = null): Configuration {
        // 如果配置不存在, 直接释放即可, 并不需要任何检查操作
        if (!newFile(getDataFolder(), path, create = false).exists()) {
            return Configuration.loadFromFile(releaseResourceFile(path))
        }
        val configFile = newFile(getDataFolder(), path)

        // 如果没开启自动更新则直接加载
        if (!autoUpdate) {
            return Configuration.loadFromFile(configFile)
        }

        // 由 Bukkit Configuration 加载配置, 防止注释丢失
        val config = YamlConfiguration.loadConfiguration(configFile)

        // 读取 Jar 包内的对应配置文件
        val cachedFile = cache ?: Configuration.loadFromInputStream(javaClass.classLoader.getResourceAsStream(path.replace('\\', '/')) ?: return Configuration.loadFromOther(config))

        val updated = mutableListOf<String>()
        read(cachedFile, config, updateNodes, updated)
        if (updated.isNotEmpty()) {
            config.save(configFile)
        }

        return Configuration.loadFromOther(config)
    }

    private fun read(cache: ConfigurationSection, to: org.bukkit.configuration.ConfigurationSection, updateNodes: List<String>, updated: MutableList<String>) {
        for (key in cache.getKeys(true)) {
            val root = key.split(".").first()
            if (root !in updateNodes && key !in updateNodes) {
                continue
            }
            // 旧版没有, 添加
            if (!to.contains(key)) {
                updated += "$key (+)"
                to[key] = cache[key]
                continue
            }

            if (cache[key] == null) {
                updated += "$key (${to[key]} -> null)"
                to[key] = null
                continue
            }

            val read = cache[key]
            if (to[key] == read) continue
            to[key] = read
            updated += "$key (${to[key]} -> $read)"
        }
    }
}