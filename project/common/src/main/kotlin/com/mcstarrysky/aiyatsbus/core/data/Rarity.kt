package com.mcstarrysky.aiyatsbus.core.data

import com.mcstarrysky.aiyatsbus.core.StandardPriorities
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.configuration.Configuration
import java.util.concurrent.ConcurrentHashMap

/**
 * 附魔品质
 *
 * @author mical
 * @since 2024/2/17 14:19
 */
data class Rarity(
    val name: String,
    val color: String,
    val weight: Int,
    val skull: String
) {

    @Transient
    lateinit var id: String

    fun display(): String {
        return "$color$name"
    }

    companion object {

        val rarities = ConcurrentHashMap<String, Rarity>()

        @Awake(LifeCycle.CONST)
        fun init() {
            registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.RARITY) {
                Configuration.loadFromFile(releaseResourceFile("enchants/rarity.yml", false))
                    .let { config ->
                        config.getKeys(false)
                            .map { it to Configuration.deserialize<Rarity>(config.getConfigurationSection(it)!!, ignoreConstructor = true).apply { id = it } }
                    }
                    .let {
                        rarities.clear()
                        rarities.putAll(it)
                    }
            }
        }

        fun getRarity(identifier: String): Rarity? {
            return rarities[identifier] ?: rarities.values.firstOrNull { it.name == identifier }
        }
    }
}