package com.mcstarrysky.aiyatsbus.impl

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.compat.EnchantRegistrationHooks
import com.mcstarrysky.aiyatsbus.core.util.Reloadable
import com.mcstarrysky.aiyatsbus.core.util.FileWatcher.unwatch
import com.mcstarrysky.aiyatsbus.core.util.FileWatcher.watch
import com.mcstarrysky.aiyatsbus.core.util.deepRead
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.io.newFolder
import taboolib.common.io.runningResourcesInJar
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.function.*
import taboolib.module.configuration.Configuration
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.util.onlinePlayers
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

    private val FILES = ConcurrentHashMap<String, File>()

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
        clearEnchantments()

        if (newFolder(getDataFolder(), "enchants").listFiles()?.none { it.isDirectory } == true) {
            if (AiyatsbusSettings.autoReleaseEnchants) {
                runningResourcesInJar.keys.filter {
                    it.endsWith(".yml")
                            && it.startsWith("enchants/")
                            && it.count { c -> c == '/' } >= 2
                }.forEach { releaseResourceFile(it) }
            }
        }

        val time = System.currentTimeMillis()

        (newFolder(getDataFolder(), "enchants")
            .listFiles { dir, _ -> dir.isDirectory }?.toList() ?: emptyList())
            .map { it.deepRead("yml") }
            .map{ it.toList() }
            .flatten()
            .let { files ->
                for (file in files) {
                    val config = Configuration.loadFromFile(file)
                    val id = config["basic.id"].toString()

                    val enchant = AiyatsbusEnchantmentBase(id, config)
                    if (!enchant.dependencies.checkAvailable()) continue

                    val enchantment = Aiyatsbus.api().getEnchantmentRegisterer().register(enchant) as AiyatsbusEnchantment

                    BY_ID[id] = enchantment
                    BY_NAME[enchantment.basicData.name] = enchantment
                    FILES[id] = file

                    file.watch {
                        val time0 = System.currentTimeMillis()

                        val enchantName = BY_ID[id]?.basicData?.name
                        BY_ID[id]!!.trigger.onDisable()
                        Aiyatsbus.api().getEnchantmentRegisterer().unregister(BY_ID[id]!!)
                        BY_ID.remove(id)
                        BY_NAME.remove(enchantName)

                        val newEnchant = AiyatsbusEnchantmentBase(id, Configuration.loadFromFile(it))
                        if (!newEnchant.dependencies.checkAvailable()) return@watch

                        val newEnchantment = Aiyatsbus.api().getEnchantmentRegisterer().register(newEnchant) as AiyatsbusEnchantment
                        BY_ID[id] = newEnchantment
                        BY_NAME[newEnchantment.basicData.name] = newEnchantment

                        onlinePlayers.forEach(Player::updateInventory)

                        console().sendLang("enchantment-reload", id, System.currentTimeMillis() - time0)
                        EnchantRegistrationHooks.unregisterHooks()
                        EnchantRegistrationHooks.registerHooks()
                    }
                }
            }

        console().sendLang("loading-enchantments", BY_ID.size, System.currentTimeMillis() - time)
        EnchantRegistrationHooks.registerHooks()
    }

    override fun clearEnchantments() {
        for (enchant in BY_ID.values) {
            enchant.trigger.onDisable()
            Aiyatsbus.api().getEnchantmentRegisterer().unregister(enchant)
        }
        BY_ID.clear()
        BY_NAME.clear()

        FILES.values.forEach { it.unwatch() }
        FILES.clear()
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusEnchantmentManager>(DefaultAiyatsbusEnchantmentManager())
        }

        @Reloadable
        @Awake(LifeCycle.CONST)
        fun load() {
            registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.ENCHANTMENT) {
                Aiyatsbus.api().getEnchantmentManager().loadEnchantments()
            }
        }

        @Awake(LifeCycle.DISABLE)
        fun unload() {
            Aiyatsbus.api().getEnchantmentManager().clearEnchantments()
        }
    }
}