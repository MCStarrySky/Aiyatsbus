package com.mcstarrysky.aiyatsbus.core.data

import com.mcstarrysky.aiyatsbus.core.StandardPriorities
import com.mcstarrysky.aiyatsbus.core.util.Reloadable
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
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
    val id: String,
    val name: String,
    val capability: Int,
    val activeSlots: List<EquipmentSlot>,
    val types: List<Material>,
    val skull: String
) {

    companion object {

        val targets = ConcurrentHashMap<String, Target>()

        @Reloadable
        @Awake(LifeCycle.CONST)
        fun init() {
            registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.TARGET) {
                val time = System.currentTimeMillis()
                Configuration.loadFromFile(releaseResourceFile("enchants/target.yml", false))
                    .let { config ->
                        config.getKeys(false)
                            .map { it to Target(it, config.getString("$it.name")!!, config.getInt("$it.max"),
                                config.getStringList("$it.active_slots").map { EquipmentSlot.valueOf(it) },
                                config.getStringList("$it.types").map { Material.valueOf(it) },
                                config.getString("$it.skull") ?: ""
                            ) }
                    }
                    .let {
                        targets.clear()
                        targets.putAll(it)
                    }
                info("Loaded ${targets.size} targets in ${System.currentTimeMillis() - time}ms")
            }
        }
    }
}