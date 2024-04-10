package com.mcstarrysky.aiyatsbus.module.migrate.nereusopus.enchantments

import com.mcstarrysky.aiyatsbus.core.data.VariableType

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.migrate.nereusopus.enchantments.EnchantmentTransfer
 *
 * @author mical
 * @since 2024/4/11 00:07
 */
interface EnchantmentTransfer {

    /** NereusOpus 节点名称 对 变量类型 to 节点名称 */
    val transfer: Map<String, Pair<VariableType, String>>
}