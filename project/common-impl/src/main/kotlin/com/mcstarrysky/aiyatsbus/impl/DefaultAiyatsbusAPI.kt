package com.mcstarrysky.aiyatsbus.impl

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.registration.AiyatsbusEnchantmentRegisterer
import com.mcstarrysky.aiyatsbus.core.registration.modern.ModernEnchantmentRegisterer
import com.mcstarrysky.aiyatsbus.impl.registration.legacy.DefaultLegacyEnchantmentRegisterer
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.library.reflex.Reflex.Companion.invokeConstructor
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

    private var enchantmentManager = PlatformFactory.getAPI<AiyatsbusEnchantmentManager>()

    private val displayManager = PlatformFactory.getAPI<AiyatsbusDisplayManager>()

    private val playerDataHandler = PlatformFactory.getAPI<AiyatsbusPlayerDataHandler>()

    private val enchantmentRegisterer: AiyatsbusEnchantmentRegisterer = if (MinecraftVersion.majorLegacy <= 12002) {
        DefaultLegacyEnchantmentRegisterer
    } else run {
        nmsProxy(Class.forName("com.mcstarrysky.aiyatsbus.impl.registration.modern.DefaultModernEnchantmentRegisterer")) as ModernEnchantmentRegisterer
    }

    override fun getEnchantmentFilter(): AiyatsbusEnchantmentFilter {
        return enchantmentFilter
    }

    override fun getEnchantmentManager(): AiyatsbusEnchantmentManager {
        return enchantmentManager
    }

    override fun getEnchantmentRegisterer(): AiyatsbusEnchantmentRegisterer {
        return enchantmentRegisterer
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

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            if (MinecraftVersion.majorLegacy >= 12003) {
                val reg = nmsProxy(Class.forName("com.mcstarrysky.aiyatsbus.impl.registration.modern.DefaultModernEnchantmentRegisterer")) as ModernEnchantmentRegisterer
                reg.replaceRegistry()
            }
        }
    }
}