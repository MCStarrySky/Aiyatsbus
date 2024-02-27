package com.mcstarrysky.aiyatsbus.core.data

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.StandardPriorities
import com.mcstarrysky.aiyatsbus.core.aiyatsbusEt
import com.mcstarrysky.aiyatsbus.core.aiyatsbusEts
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.configuration.Configuration
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.data.Group
 *
 * @author mical
 * @since 2024/2/18 11:09
 */
data class Group(
    val name: String,
    val enchantments: List<AiyatsbusEnchantment>,
    val skullBase64: String,
    val maxCoexist: Int
) {

    companion object {

        val groups = ConcurrentHashMap<String, Group>()

        @Awake(LifeCycle.CONST)
        fun load() {
            registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.GROUP) {
                Configuration.loadFromFile(releaseResourceFile("enchants/group.yml", false))
                    .let { config ->
                        config.getKeys(false).map { name ->
                            val enchants = config.getStringList("$name.enchants").mapNotNull(::aiyatsbusEt).toMutableList()
                            enchants += config.getStringList("$name.rarities").map { Rarity.getRarity(it)?.let { r -> aiyatsbusEts(r) } ?: listOf() }.flatten()
                            val skull = config.getString("$name.skull", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzRiODlhZDA2ZDMxOGYwYWUxZWVhZjY2MGZlYTc4YzM0ZWI1NWQwNWYwMWUxY2Y5OTlmMzMxZmIzMmQzODk0MiJ9fX0=")!!
                            name to Group(name, enchants, skull, config.getInt("$name.max_coexist", 1))
                        }
                    }
                    .let {
                        groups.clear()
                        groups.putAll(it)
                    }
            }
        }
    }
}