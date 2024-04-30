package com.mcstarrysky.aiyatsbus.module.compat.registration

import com.earth2me.essentials.Enchantments
import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.compat.EnchantRegistrationHook
import com.mcstarrysky.aiyatsbus.core.compat.EnchantRegistrationHooks
import org.bukkit.enchantments.Enchantment
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.library.reflex.Reflex.Companion.getProperty

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.compat.registration.EssentialsRegistrationHook
 *
 * @author mical
 * @since 2024/4/30 21:34
 */
class EssentialsRegistrationHook : EnchantRegistrationHook {

    private val fields = listOf("ENCHANTMENTS", "ALIASENCHANTMENTS")

    override fun getPluginName(): String = "Essentials"

    override fun register() {
        for (enchantment in Aiyatsbus.api().getEnchantmentManager().getByIDs().values) {
            for (field in fields) {
                Enchantments::class.java.getProperty<MutableMap<String, Enchantment>>(field)?.let {
                    it[enchantment.basicData.id] = enchantment.enchantment
                    it[enchantment.basicData.name] = enchantment.enchantment
                }
            }
        }
    }

    override fun unregister() {
        for (enchantment in Aiyatsbus.api().getEnchantmentManager().getByIDs().values) {
            for (field in fields) {
                Enchantments::class.java.getProperty<MutableMap<String, Enchantment>>(field)?.let {
                    it -= enchantment.basicData.id
                    it -= enchantment.basicData.name
                }
            }
        }
    }

    companion object {

        @Awake(LifeCycle.LOAD)
        fun init() {
            EnchantRegistrationHooks.registerHook(EssentialsRegistrationHook())
        }
    }
}