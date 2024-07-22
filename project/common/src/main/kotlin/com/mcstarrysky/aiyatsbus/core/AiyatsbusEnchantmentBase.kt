package com.mcstarrysky.aiyatsbus.core

import com.mcstarrysky.aiyatsbus.core.data.*
import com.mcstarrysky.aiyatsbus.core.data.registry.Target
import com.mcstarrysky.aiyatsbus.core.data.registry.Rarity
import com.mcstarrysky.aiyatsbus.core.data.trigger.Trigger
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import taboolib.module.configuration.Configuration
import java.io.File

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentBase
 *
 * @author mical
 * @since 2024/2/17 14:39
 */
class AiyatsbusEnchantmentBase(
    override val id: String,
    override val file: File,
    override val config: Configuration
) : AiyatsbusEnchantment {

    override val enchantmentKey: NamespacedKey = NamespacedKey.minecraft(id)

    override val basicData: BasicData = BasicData(config.getConfigurationSection("basic")!!)

    override val alternativeData: AlternativeData = AlternativeData(config.getConfigurationSection("alternative"))

    override val dependencies: Dependencies = Dependencies(config.getConfigurationSection("dependencies"))

    override lateinit var enchantment: Enchantment

    override val rarity: Rarity
        get() = aiyatsbusRarity(config["rarity"].toString()) ?: aiyatsbusRarity(AiyatsbusSettings.defaultRarity) ?: error("Enchantment $id has an unknown rarity")

    override val variables: Variables = Variables.load(config.getConfigurationSection("variables"))

    override val displayer: Displayer = Displayer(config.getConfigurationSection("display")!!, this)

    override val targets: List<Target>
        get() = config.getStringList("targets").mapNotNull(::aiyatsbusTarget)

    override val limitations: Limitations = Limitations(this, config.getStringList("limitations"))

    override val trigger: Trigger = Trigger(config.getConfigurationSection("mechanisms"), this)
}