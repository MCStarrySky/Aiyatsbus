package com.mcstarrysky.aiyatsbus.core

import com.mcstarrysky.aiyatsbus.core.data.*
import com.mcstarrysky.aiyatsbus.core.data.Target
import com.mcstarrysky.aiyatsbus.core.trigger.Trigger
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import taboolib.module.configuration.Configuration

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentBase
 *
 * @author mical
 * @since 2024/2/17 14:39
 */
class AiyatsbusEnchantmentBase(
    override val id: String,
    config: Configuration
) : AiyatsbusEnchantment {

    override val enchantmentKey: NamespacedKey = NamespacedKey.minecraft(id)

    override val basicData: BasicData = BasicData.load(config.getConfigurationSection("basic")!!)

    override val alternativeData: AlternativeData = AlternativeData(config.getConfigurationSection("alternative"))

    override lateinit var enchantment: Enchantment

    override val rarity: Rarity = Rarity.getRarity(config["rarity"].toString()) ?: Rarity.rarities[AiyatsbusSettings.defaultRarity] ?: error("Enchantment $id has an unknown rarity")

    override val variables: Variables = Variables.load(config.getConfigurationSection("variables"))

    override val displayer: Displayer = Displayer.load(config.getConfigurationSection("display")!!, this)

    override val targets: List<Target> = config.getStringList("targets").mapNotNull { t -> Target.targets.values.firstOrNull { it.name == t } ?: Target.targets[t] }

    override val limitations: Limitations = Limitations(this, config.getStringList("limitations"))

    override val trigger: Trigger = Trigger(config.getConfigurationSection("mechanisms"), this)
}