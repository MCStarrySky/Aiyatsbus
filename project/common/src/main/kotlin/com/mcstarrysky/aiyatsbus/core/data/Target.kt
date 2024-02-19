package com.mcstarrysky.aiyatsbus.core.data

import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.common.platform.function.releaseResourceFile
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
    val name: String,
    val capability: Int,
    private val active_slots: List<String>,
    private val types: List<String>,
    val skull: String
) {

    @Transient
    lateinit var id: String

    @Transient
    lateinit var activeSlots: List<EquipmentSlot>

    @Transient
    lateinit var itemTypes: List<Material>

    companion object {

        val targets = ConcurrentHashMap<String, Target>()

        @Awake(LifeCycle.CONST)
        fun init() {
            registerLifeCycleTask(LifeCycle.ENABLE, 1) {
                Configuration.loadFromFile(releaseResourceFile("enchants/target.yml", false))
                    .let { config ->
                        config.getKeys(false)
                            .map { it to Configuration.deserialize<Target>(config.getConfigurationSection(it)!!, ignoreConstructor = true).apply {
                                id = it
                                activeSlots = active_slots.map(EquipmentSlot::valueOf)
                                itemTypes = types.map(Material::valueOf)
                            } }
                    }
                    .let {
                        targets.clear()
                        targets.putAll(it)
                    }
            }
        }
    }
}