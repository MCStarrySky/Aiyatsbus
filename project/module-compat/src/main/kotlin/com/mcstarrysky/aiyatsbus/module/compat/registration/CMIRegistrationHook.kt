package com.mcstarrysky.aiyatsbus.module.compat.registration

import com.mcstarrysky.aiyatsbus.core.compat.EnchantRegistrationHook
import com.mcstarrysky.aiyatsbus.core.compat.EnchantRegistrationHooks
import net.Zrips.CMILib.Enchants.CMIEnchantment
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.compat.registration.CMIRegistrationHook
 *
 * @author mical
 * @since 2024/4/30 21:33
 */
class CMIRegistrationHook : EnchantRegistrationHook {

    override fun getPluginName(): String = "CMI"

    override fun register() {
        CMIEnchantment.initialize()
        CMIEnchantment.saveEnchants()
    }

    companion object {

        @Awake(LifeCycle.LOAD)
        fun init() {
            EnchantRegistrationHooks.registerHook(CMIRegistrationHook())
        }
    }
}