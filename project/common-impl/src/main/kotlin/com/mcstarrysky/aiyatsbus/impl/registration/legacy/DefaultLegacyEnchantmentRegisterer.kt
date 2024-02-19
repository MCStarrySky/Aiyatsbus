package com.mcstarrysky.aiyatsbus.impl.registration.legacy

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentBase
import com.mcstarrysky.aiyatsbus.core.registration.AiyatsbusEnchantmentRegisterer
import org.bukkit.enchantments.Enchantment
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.setProperty

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.registration.legacy.DefaultLegacyEnchantmentRegisterer
 *
 * @author mical
 * @since 2024/2/17 18:51
 */
object DefaultLegacyEnchantmentRegisterer : AiyatsbusEnchantmentRegisterer {

    @Awake(LifeCycle.CONST)
    fun init() {
        Enchantment::class.java.setProperty("acceptingNew", value = true, isStatic = true)
    }

    @Awake(LifeCycle.DISABLE)
    fun exit() {
        Enchantment::class.java.setProperty("acceptingNew", value = false, isStatic = true)
    }

    override fun register(enchant: AiyatsbusEnchantmentBase): Enchantment {
        val enchantment = LegacyCraftEnchantment(enchant)
        // 不需要向 Bukkit 注册原版附魔
        if (!enchant.alternativeData.isVanilla) {
            Enchantment.registerEnchantment(enchantment)
        }
        return enchantment
    }

    override fun unregister(enchant: AiyatsbusEnchantment) {
        // 肯定不能卸载原版附魔啊, 想什么呢?
        if (!enchant.alternativeData.isVanilla) {
            Enchantment::class.java.getProperty<HashMap<*, *>>("byKey", true)!!.remove(enchant.enchantmentKey)
            Enchantment::class.java.getProperty<HashMap<*, *>>("byName", true)!!.remove(enchant.id.uppercase())
        }
    }
}