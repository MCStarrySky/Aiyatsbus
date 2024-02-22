package com.mcstarrysky.aiyatsbus.impl

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentBase
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentManager
import taboolib.common.LifeCycle
import taboolib.common.io.newFolder
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.module.configuration.Configuration
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.DefaultAiyatsbusEnchantmentManager
 *
 * @author mical
 * @since 2024/2/17 16:22
 */
class DefaultAiyatsbusEnchantmentManager : AiyatsbusEnchantmentManager {

    private val BY_ID = ConcurrentHashMap<String, AiyatsbusEnchantment>()
    private val BY_NAME = ConcurrentHashMap<String, AiyatsbusEnchantment>()

    override fun getByIDs(): Map<String, AiyatsbusEnchantment> {
        return BY_ID
    }

    override fun getByID(id: String): AiyatsbusEnchantment? {
        return BY_ID[id]
    }

    override fun getByNames(): Map<String, AiyatsbusEnchantment> {
        return BY_NAME
    }

    override fun getByName(name: String): AiyatsbusEnchantment? {
        return BY_NAME[name]
    }

    override fun register(enchantment: AiyatsbusEnchantmentBase) {
        Aiyatsbus.api().getEnchantmentRegisterer().register(enchantment)
        BY_ID[enchantment.enchantmentKey.key] = enchantment as AiyatsbusEnchantment
        BY_NAME[enchantment.basicData.name] = enchantment as AiyatsbusEnchantment
    }

    override fun loadEnchantments() {
        (newFolder(getDataFolder(), "enchants")
            .listFiles { dir, _ -> dir.isDirectory }?.toList() ?: emptyList())
            .map { it.listFiles { _, name -> name.endsWith(".yml") } }
            .map{ it?.toList() ?: emptyList() }
            .flatten()
            .forEach { file ->
                val config = Configuration.loadFromFile(file)
                val id = config["basic.id"].toString()
                val enchant = AiyatsbusEnchantmentBase(id, config)
                val enchantment = Aiyatsbus.api().getEnchantmentRegisterer().register(enchant) as AiyatsbusEnchantment

                BY_ID[id] = enchantment
                BY_NAME[enchantment.basicData.name] = enchantment
            }
    }

    override fun clearEnchantments() {
        for (enchant in BY_ID.values) {
            Aiyatsbus.api().getEnchantmentRegisterer().unregister(enchant)
        }
        BY_ID.clear()
        BY_NAME.clear()
    }

    init {
        registerLifeCycleTask(LifeCycle.ENABLE, 3) { loadEnchantments() }
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusEnchantmentManager>(DefaultAiyatsbusEnchantmentManager())
        }
    }
}