package com.mcstarrysky.aiyatsbus.module.migrate.nereusopus.enchantments.simple

import com.mcstarrysky.aiyatsbus.core.data.VariableType
import com.mcstarrysky.aiyatsbus.module.migrate.nereusopus.enchantments.EnchantmentTransfer
import taboolib.module.configuration.Configuration
import java.io.File

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.migrate.nereusopus.enchantments.simple.BlastMining
 *
 * @author mical
 * @since 2024/4/11 00:10
 */
object BlastMining : EnchantmentTransfer {

    override val transfer: Map<String, Pair<VariableType, String>> = mapOf(
        "disable-on-sneak" to (VariableType.ORDINARY to "disable-on-sneaking"),
        "per-tick" to (VariableType.ORDINARY to "per-tick"),
        "hardness-check" to (VariableType.ORDINARY to "hardness-check"),
        "blacklist" to (VariableType.ORDINARY to "blacklist"),
        "range_x" to (VariableType.LEVELED to "X范围"),
        "range_y" to (VariableType.LEVELED to "Y范围"),
        "range_z" to (VariableType.LEVELED to "Z范围")
    )

    override val file: File
        get() = TODO("Not yet implemented")

    override val config: Configuration
        get() = TODO("Not yet implemented")
}