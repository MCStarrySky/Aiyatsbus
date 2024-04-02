package com.mcstarrysky.aiyatsbus.impl

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.registration.AiyatsbusEnchantmentRegisterer
import com.mcstarrysky.aiyatsbus.core.registration.modern.ModernEnchantmentRegisterer
import com.mcstarrysky.aiyatsbus.impl.registration.legacy.DefaultLegacyEnchantmentRegisterer
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.function.info
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.nmsProxy
import java.util.concurrent.CompletableFuture

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.DefaultAiyatsbusAPI
 *
 * @author mical
 * @since 2024/2/17 16:20
 */
class DefaultAiyatsbusAPI : AiyatsbusAPI {

    private val enchantmentFilter = PlatformFactory.getAPI<AiyatsbusEnchantmentFilter>()

    private val enchantmentManager = PlatformFactory.getAPI<AiyatsbusEnchantmentManager>()

    private val displayManager = PlatformFactory.getAPI<AiyatsbusDisplayManager>()

    private val language = PlatformFactory.getAPI<AiyatsbusLanguage>()

    private val playerDataHandler = PlatformFactory.getAPI<AiyatsbusPlayerDataHandler>()

    private val enchantmentRegisterer: AiyatsbusEnchantmentRegisterer = if (MinecraftVersion.majorLegacy <= 12002) {
        DefaultLegacyEnchantmentRegisterer
    } else run {
        proxy<ModernEnchantmentRegisterer>("com.mcstarrysky.aiyatsbus.impl.registration.v12004_nms.DefaultModernEnchantmentRegisterer")
    }

    private val eventExecutor = PlatformFactory.getAPI<AiyatsbusEventExecutor>()

    private val ketherHandler = PlatformFactory.getAPI<AiyatsbusKetherHandler>()

    private val mcAPI by lazy {
        proxy<AiyatsbusMinecraftAPI>("com.mcstarrysky.aiyatsbus.impl.nms.DefaultAiyatsbusMinecraftAPI")
    }

    private val tickHandler = PlatformFactory.getAPI<AiyatsbusTickHandler>()

    override fun getEnchantmentFilter(): AiyatsbusEnchantmentFilter {
        return enchantmentFilter
    }

    override fun getEnchantmentManager(): AiyatsbusEnchantmentManager {
        return enchantmentManager
    }

    override fun getEnchantmentRegisterer(): AiyatsbusEnchantmentRegisterer {
        return enchantmentRegisterer
    }

    override fun getEventExecutor(): AiyatsbusEventExecutor {
        return eventExecutor
    }

    override fun getKetherHandler(): AiyatsbusKetherHandler {
        return ketherHandler
    }

    override fun getDisplayManager(): AiyatsbusDisplayManager {
        return displayManager
    }

    override fun getLanguage(): AiyatsbusLanguage {
        return language
    }

    override fun getMinecraftAPI(): AiyatsbusMinecraftAPI {
        return mcAPI
    }

    override fun getPlayerDataHandler(): AiyatsbusPlayerDataHandler {
        return playerDataHandler
    }

    override fun getTickHandler(): AiyatsbusTickHandler {
        return tickHandler
    }

    private inline fun <reified T> proxy(bind: String, vararg parameter: Any): T {
        val time = System.currentTimeMillis()
        val proxy = nmsProxy(T::class.java, bind, *parameter)
        info("Generated ${T::class.java.simpleName} in ${System.currentTimeMillis() - time}ms")
        return proxy
    }

    init {
        CompletableFuture.runAsync {
            enchantmentRegisterer
            mcAPI
        }
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            if (MinecraftVersion.majorLegacy >= 12003) {
                val reg = nmsProxy<ModernEnchantmentRegisterer>("com.mcstarrysky.aiyatsbus.impl.registration.v12004_nms.DefaultModernEnchantmentRegisterer")
                reg.replaceRegistry()
            }
        }
    }
}