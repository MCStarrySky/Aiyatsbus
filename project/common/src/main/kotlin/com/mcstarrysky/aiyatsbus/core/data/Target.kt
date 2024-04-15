package com.mcstarrysky.aiyatsbus.core.data

import com.mcstarrysky.aiyatsbus.core.StandardPriorities
import com.mcstarrysky.aiyatsbus.core.sendLang
import com.mcstarrysky.aiyatsbus.core.util.Reloadable
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot
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
 * com.mcstarrysky.aiyatsbus.core.data.Target
 *
 * @author mical
 * @since 2024/2/17 23:32
 */
data class Target(
    private val root: ConfigurationSection,
    val id: String = root.name,
    val name: String = root.getString("name")!!,
    val capability: Int = root.getInt("max"),
    val activeSlots: List<EquipmentSlot> = root.getStringList("active_slots").map { EquipmentSlot.valueOf(it) },
    val types: List<Material> = root.getStringList("types").map { Material.valueOf(it) },
    val skull: String = root.getString("skull", "")!!
)

object TargetLoader {

    @Config("enchants/target.yml")
    lateinit var config: Configuration
        private set

    val targets = ConcurrentHashMap<String, Target>()

    @Reloadable
    @Awake(LifeCycle.CONST)
    fun init() {
        registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.TARGET) {
            load()
        }
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
        targets.clear()
        targets += config.getKeys(false).map { config.getConfigurationSection(it)!! }.map { it.name to Target(it) }
        console().sendLang("loading-targets", targets.size, System.currentTimeMillis() - time)
    }
}