package com.mcstarrysky.aiyatsbus.module.kether.action.game

import com.mcstarrysky.aiyatsbus.core.compat.GuardItemChecker
import org.bukkit.inventory.ItemStack
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.game.ActionGuardItemChecker
 *
 * @author mical
 * @since 2024/8/19 10:19
 */
object ActionGuardItemChecker {

    @KetherParser(["is-guard-item"], shared = true)
    fun guardItemParser() = combinationParser {
        it.group(type<ItemStack>()).apply(it) { item -> now { GuardItemChecker.checkIsGuardItem(item) } }
    }
}