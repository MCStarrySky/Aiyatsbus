package com.mcstarrysky.aiyatsbus.core.data.registry

import com.mcstarrysky.aiyatsbus.core.StandardPriorities
import com.mcstarrysky.aiyatsbus.core.data.Dependencies
import com.mcstarrysky.aiyatsbus.core.sendLang
import com.mcstarrysky.aiyatsbus.core.util.inject.AwakePriority
import com.mcstarrysky.aiyatsbus.core.util.inject.Reloadable
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.xseries.XMaterial
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import java.util.concurrent.ConcurrentHashMap
import kotlin.jvm.optionals.getOrNull

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.data.Target
 *
 * @author mical
 * @since 2024/2/17 23:32
 */
data class Target @JvmOverloads constructor(
    private val root: ConfigurationSection,
    val id: String = root.name,
    val name: String = root.getString("name")!!,
    val capability: Int = root.getInt("max"),
    val activeSlots: List<EquipmentSlot> = root.getStringList("active_slots").map { EquipmentSlot.valueOf(it) },
    val types: List<Material> = root.getStringList("types").mapNotNull { XMaterial.matchXMaterial(it).getOrNull()?.parseMaterial() },
    val skull: String = root.getString("skull", "")!!,
    val dependencies: Dependencies = Dependencies(root.getConfigurationSection("dependencies"))
)

object TargetLoader {

    @Config("enchants/target.yml", autoReload = true)
    lateinit var config: Configuration
        private set

    val registered: ConcurrentHashMap<String, Target> = ConcurrentHashMap()

    private var isLoaded = false

    @Reloadable
    @AwakePriority(LifeCycle.ENABLE, StandardPriorities.TARGET)
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
        for (section in config.getKeys(false).map { config.getConfigurationSection(it)!! }) {
            val target = Target(section)
            if (!target.dependencies.checkAvailable()) {
                continue
            }
            registered += target.id to target
        }
        console().sendLang("loading-targets", registered.size, System.currentTimeMillis() - time)
    }
}