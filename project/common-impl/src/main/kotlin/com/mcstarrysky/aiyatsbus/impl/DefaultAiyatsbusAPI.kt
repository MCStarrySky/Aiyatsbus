package com.mcstarrysky.aiyatsbus.impl

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.registration.AiyatsbusEnchantmentRegisterer
import com.mcstarrysky.aiyatsbus.core.registration.modern.ModernEnchantmentRegisterer
import com.mcstarrysky.aiyatsbus.impl.registration.legacy.DefaultLegacyEnchantmentRegisterer
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.module.lang.Language
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.nmsProxy

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

    private val playerDataHandler = PlatformFactory.getAPI<AiyatsbusPlayerDataHandler>()

    private val enchantmentRegisterer: AiyatsbusEnchantmentRegisterer = if (MinecraftVersion.majorLegacy <= 12002) {
        DefaultLegacyEnchantmentRegisterer
    } else run {
        nmsProxy<ModernEnchantmentRegisterer>("com.mcstarrysky.aiyatsbus.impl.registration.v12004_nms.DefaultModernEnchantmentRegisterer")
    }

    private val eventExecutor = PlatformFactory.getAPI<AiyatsbusEventExecutor>()

    private val ketherHandler = PlatformFactory.getAPI<AiyatsbusKetherHandler>()

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

    override fun getMinecraftAPI(): AiyatsbusMinecraftAPI {
        return nmsProxy<AiyatsbusMinecraftAPI>("com.mcstarrysky.aiyatsbus.impl.nms.DefaultAiyatsbusMinecraftAPI")
    }

    override fun getPlayerDataHandler(): AiyatsbusPlayerDataHandler {
        return playerDataHandler
    }

    override fun getTickHandler(): AiyatsbusTickHandler {
        return tickHandler
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            Language.path = "core/lang"
            Language.enableSimpleComponent = true
            if (MinecraftVersion.majorLegacy >= 12003) {
                val reg = nmsProxy<ModernEnchantmentRegisterer>("com.mcstarrysky.aiyatsbus.impl.registration.v12004_nms.DefaultModernEnchantmentRegisterer")
                reg.replaceRegistry()
            }
        }
    }
}