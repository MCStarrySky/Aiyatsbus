/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.mcstarrysky.aiyatsbus.impl

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.compat.EnchantRegistrationHooks
import com.mcstarrysky.aiyatsbus.core.registration.modern.ModernEnchantmentRegisterer
import com.mcstarrysky.aiyatsbus.core.util.FileWatcher.isProcessingByWatcher
import com.mcstarrysky.aiyatsbus.core.util.inject.Reloadable
import com.mcstarrysky.aiyatsbus.core.util.FileWatcher.unwatch
import com.mcstarrysky.aiyatsbus.core.util.FileWatcher.watch
import com.mcstarrysky.aiyatsbus.core.util.YamlUpdater
import com.mcstarrysky.aiyatsbus.core.util.deepRead
import com.mcstarrysky.aiyatsbus.core.util.inject.AwakePriority
import com.mcstarrysky.aiyatsbus.impl.enchant.InternalAiyatsbusEnchantment
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.io.newFolder
import taboolib.common.io.runningResourcesInJar
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.function.*
import taboolib.module.configuration.Configuration
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.nmsProxy
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

    private val values = ConcurrentHashMap<NamespacedKey, AiyatsbusEnchantment>()

    override fun getEnchants(): Map<NamespacedKey, AiyatsbusEnchantment> {
        return values
    }

    override fun getEnchant(key: NamespacedKey): AiyatsbusEnchantment? {
        return getEnchant(key.key)
    }

    override fun getEnchant(key: String): AiyatsbusEnchantment? {
        return values.values.firstOrNull { it.basicData.id == key }
    }

    override fun getByName(name: String): AiyatsbusEnchantment? {
        return values.values.firstOrNull { it.basicData.name == name }
    }

    override fun register(enchantment: AiyatsbusEnchantmentBase) {
        values[enchantment.enchantmentKey] = Aiyatsbus.api().getEnchantmentRegisterer().register(enchantment) as AiyatsbusEnchantment
        EnchantRegistrationHooks.registerHooks()
    }

    override fun unregister(enchantment: AiyatsbusEnchantment) {
        enchantment.trigger?.onDisable()
        Aiyatsbus.api().getEnchantmentRegisterer().unregister(enchantment)
        values -= enchantment.enchantmentKey
    }

    override fun loadEnchantments() {
        clearEnchantments()

        if (newFolder(getDataFolder(), "enchants").listFiles()?.none { it.isDirectory } == true) {
            if (AiyatsbusSettings.autoReleaseEnchants) {
                runningResourcesInJar.keys.filter {
                    val path = it.replace("/", File.separator)
                    path.endsWith(".yml")
                            && path.startsWith("enchants" + File.separator)
                            && path.count { c -> c == File.separatorChar } >= 2
                }.forEach { releaseResourceFile(it) }
            }
        }

        val time = System.currentTimeMillis()

        (newFolder(getDataFolder(), "enchants")
            .listFiles { dir, _ -> dir.isDirectory }?.toList() ?: emptyList())
            .map { it.deepRead("yml") }
            .map{ it.toList() }
            .flatten()
            .forEach(::loadFromFile)

        console().sendLang("loading-enchantments", values.size, System.currentTimeMillis() - time)
    }

    override fun loadFromFile(file: File) {
        val path = file.path.substring(file.path.indexOf("enchants" + File.separator), file.path.length)
        val config = YamlUpdater.loadFromFile(path, AiyatsbusSettings.enableUpdater, AiyatsbusSettings.updateContents)
        val id = config["basic.id"].toString()
        val key = NamespacedKey.minecraft(id)

        val enchant = InternalAiyatsbusEnchantment(id, file, config)
        if (!enchant.dependencies.checkAvailable()) return

        register(enchant)

        file.watch { f ->
            if (f.isProcessingByWatcher) {
                f.isProcessingByWatcher = false
                return@watch
            }
            val resourceStream = javaClass.classLoader.getResourceAsStream(path.replace('\\', '/')) // 真傻逼啊
            if (AiyatsbusSettings.enableUpdater && resourceStream != null) {
                console().sendLang("enchantment-reload-failed", id)
                f.isProcessingByWatcher = true
                YamlUpdater.loadFromFile(path, AiyatsbusSettings.enableUpdater, AiyatsbusSettings.updateContents, Configuration.loadFromInputStream(resourceStream))
                return@watch
            }

            val time = System.currentTimeMillis()

            val ench = getEnchant(key)!!
            ench.trigger?.onDisable()
            unregister(ench)

            val newEnchant = InternalAiyatsbusEnchantment(id, f, Configuration.loadFromFile(f))
            if (!newEnchant.dependencies.checkAvailable()) return@watch
            register(newEnchant)

            onlinePlayers.forEach(Player::updateInventory)

            console().sendLang("enchantment-reload", id, System.currentTimeMillis() - time)
            EnchantRegistrationHooks.unregisterHooks()
            EnchantRegistrationHooks.registerHooks()
        }
    }

    override fun clearEnchantments() {
        for (enchant in values.values) {
            enchant.file.isProcessingByWatcher = false
            enchant.file.unwatch()
            unregister(enchant)
        }
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusEnchantmentManager>(DefaultAiyatsbusEnchantmentManager())
            val registerer = if (MinecraftVersion.majorLegacy >= 12100) {
                nmsProxy<ModernEnchantmentRegisterer>("com.mcstarrysky.aiyatsbus.impl.registration.v12100_nms.DefaultModernEnchantmentRegisterer")
            } else if (MinecraftVersion.majorLegacy >= 12003) {
                nmsProxy<ModernEnchantmentRegisterer>("com.mcstarrysky.aiyatsbus.impl.registration.v12004_nms.DefaultModernEnchantmentRegisterer")
            } else {
                return
            }
            registerer.replaceRegistry()
            registerLifeCycleTask(LifeCycle.ACTIVE) {
                registerer.replaceRegistry()
            }
            registerLifeCycleTask(LifeCycle.DISABLE) {
                Aiyatsbus.api().getEnchantmentManager().clearEnchantments()
            }
        }

        @Reloadable
        @AwakePriority(LifeCycle.ENABLE, StandardPriorities.ENCHANTMENT)
        fun onEnable() {
            Aiyatsbus.api().getEnchantmentManager().loadEnchantments()
        }
    }
}