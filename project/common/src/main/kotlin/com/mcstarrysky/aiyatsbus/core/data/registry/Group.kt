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

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.data.Dependencies
import com.mcstarrysky.aiyatsbus.core.util.inject.Reloadable
import com.mcstarrysky.aiyatsbus.core.util.inject.AwakePriority
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.data.Group
 *
 * @author mical
 * @since 2024/2/18 11:09
 */
data class Group @JvmOverloads constructor(
    private val root: ConfigurationSection,
    val name: String = root.name,
    val exclude: List<AiyatsbusEnchantment> = root.getStringList("exclude").mapNotNull(::aiyatsbusEt),
    val enchantments: List<AiyatsbusEnchantment> = root.getStringList("enchants").mapNotNull(::aiyatsbusEt)
        .toMutableList().also {
            it += root.getStringList("rarities")
                .map { aiyatsbusRarity(it)?.let { r -> aiyatsbusEts(r) } ?: listOf() }.flatten()
        }.filter { it !in exclude },
    val skull: String = root.getString(
        "skull",
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzRiODlhZDA2ZDMxOGYwYWUxZWVhZjY2MGZlYTc4YzM0ZWI1NWQwNWYwMWUxY2Y5OTlmMzMxZmIzMmQzODk0MiJ9fX0="
    )!!,
    val maxCoexist: Int = root.getInt("max_coexist", 1),
    val dependencies: Dependencies = Dependencies(root.getConfigurationSection("dependencies"))
)

object GroupLoader {

    @Config("enchants/group.yml", autoReload = true)
    lateinit var config: Configuration
        private set

    val registered: ConcurrentHashMap<String, Group> = ConcurrentHashMap()

    private var isLoaded = false

    @Reloadable
    @AwakePriority(LifeCycle.ENABLE, StandardPriorities.GROUP)
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
        registerLifeCycleTask(LifeCycle.ENABLE) {
            config.onReload {
                load()
            }
        }
    }

    private fun load() {
        val time = System.currentTimeMillis()
        registered.clear()
        config.getKeys(false).map { config.getConfigurationSection(it)!! }.forEach {
            val group = Group(it)
            if (!group.dependencies.checkAvailable()) {
                return@forEach
            }
            registered += group.name to group
        }
        console().sendLang("loading-groups", registered.size, System.currentTimeMillis() - time)
    }
}