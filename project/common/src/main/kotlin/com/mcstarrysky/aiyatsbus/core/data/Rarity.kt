package com.mcstarrysky.aiyatsbus.core.data

import com.mcstarrysky.aiyatsbus.core.StandardPriorities
import com.mcstarrysky.aiyatsbus.core.util.Reloadable
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.chat.component
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import java.util.concurrent.ConcurrentHashMap

/**
 * 附魔品质
 *
 * @author mical
 * @since 2024/2/17 14:19
 */
data class Rarity(
    private val root: ConfigurationSection,
    val id: String = root.name,
    val name: String = root.getString("name")!!,
    val color: String = root.getString("color")!!.component().buildColored().toLegacyText(),
    val weight: Int = root.getInt("weight", 100),
    val skull: String = root.getString("skull", "")!!
) {
    fun displayName(): String {
        return "$color$name"
    }
}

object RarityLoader {

    @Config("enchants/rarity.yml", autoReload = true)
    lateinit var config: Configuration
        private set

    val rarities = ConcurrentHashMap<String, Rarity>()

    @Reloadable
    @Awake(LifeCycle.CONST)
    fun init() {
        registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.RARITY) {
            load()
        }
        registerLifeCycleTask(LifeCycle.ENABLE) {
            config.onReload {
                load()
            }
        }
    }

    private fun load() {
        val time = System.currentTimeMillis()
        rarities.clear()
        rarities += config.getKeys(false).map { config.getConfigurationSection(it)!! }.map { it.name to Rarity(it) }
        info("Loaded ${rarities.size} in ${System.currentTimeMillis() - time}ms")
    }
}