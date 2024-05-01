package com.mcstarrysky.aiyatsbus.core.data.registry

import com.mcstarrysky.aiyatsbus.core.StandardPriorities
import com.mcstarrysky.aiyatsbus.core.data.Dependencies
import com.mcstarrysky.aiyatsbus.core.sendLang
import com.mcstarrysky.aiyatsbus.core.util.Reloadable
import com.mcstarrysky.aiyatsbus.core.util.container.SimpleRegistry
import org.bukkit.Bukkit
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
import taboolib.module.nms.MinecraftVersion
import java.util.concurrent.ConcurrentHashMap
import kotlin.jvm.optionals.getOrNull

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
    val types: List<Material> = root.getStringList("types").mapNotNull { XMaterial.matchXMaterial(it).getOrNull()?.parseMaterial() },
    val skull: String = root.getString("skull", "")!!,
    val dependencies: Dependencies = Dependencies(root.getConfigurationSection("dependencies"))
)

object TargetLoader : SimpleRegistry<String, Target>(ConcurrentHashMap()) {

    @Config("enchants/target.yml")
    lateinit var config: Configuration
        private set

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
        clearRegistry()
        for (section in config.getKeys(false).map { config.getConfigurationSection(it)!! }) {
            val target = Target(section)
            if (!target.dependencies.checkAvailable()) {
                continue
            }
            register(target)
        }
        console().sendLang("loading-targets", size, System.currentTimeMillis() - time)
    }

    override fun getKey(value: Target): String {
        return value.id
    }
}