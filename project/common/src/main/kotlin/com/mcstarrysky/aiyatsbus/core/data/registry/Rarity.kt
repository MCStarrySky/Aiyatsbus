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
package com.mcstarrysky.aiyatsbus.core.data.registry

import com.mcstarrysky.aiyatsbus.core.StandardPriorities
import com.mcstarrysky.aiyatsbus.core.data.Dependencies
import com.mcstarrysky.aiyatsbus.core.sendLang
import com.mcstarrysky.aiyatsbus.core.util.inject.Reloadable
import com.mcstarrysky.aiyatsbus.core.util.inject.AwakePriority
import com.mcstarrysky.aiyatsbus.core.util.replace
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.chat.component
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.util.onlinePlayers
import java.util.concurrent.ConcurrentHashMap

/**
 * 附魔品质
 *
 * @author mical
 * @since 2024/2/17 14:19
 */
data class Rarity @JvmOverloads constructor(
    private val root: ConfigurationSection,
    val id: String = root.name,
    val name: String = root.getString("name")!!,
    val color: String = root.getString("color")!!,
    val weight: Int = root.getInt("weight", 100),
    val skull: String = root.getString("skull", "")!!,
    val dependencies: Dependencies = Dependencies(root.getConfigurationSection("dependencies"))
) {

    /**
     * 显示名称
     */
    fun displayName(text: String = name): String {
        return color.replace("text" to text).component().buildColored().toLegacyText()
    }
}

object RarityLoader {

    @Config("enchants/rarity.yml", autoReload = true)
    lateinit var config: Configuration
        private set

    val registered: ConcurrentHashMap<String, Rarity> = ConcurrentHashMap()

    private var isLoaded = false

    @Reloadable
    @AwakePriority(LifeCycle.ENABLE, StandardPriorities.RARITY)
    fun init() {
        if (isLoaded) {
            config.reload()
            return
        }
        load()
        isLoaded = true
    }

    @Awake(LifeCycle.ENABLE)
    fun reload() {
        config.onReload {
            load()
            onlinePlayers.forEach(Player::updateInventory) // 要刷新显示
        }
    }

    private fun load() {
        val time = System.currentTimeMillis()
        registered.clear()
        config.getKeys(false).map { config.getConfigurationSection(it)!! }.forEach {
            val rarity = Rarity(it)
            if (!rarity.dependencies.checkAvailable()) {
                return@forEach
            }
            registered += rarity.id to rarity
        }
        console().sendLang("loading-rarities", registered.size, System.currentTimeMillis() - time)
    }
}