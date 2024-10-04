@file:Suppress("MemberVisibilityCanBePrivate")

/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.mcstarrysky.aiyatsbus.core.data

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.data.LimitType.*
import com.mcstarrysky.aiyatsbus.core.util.inject.Reloadable
import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.core.util.inject.AwakePriority
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.module.kether.compileToJexl
import taboolib.platform.compat.replacePlaceholder

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.data.Limitations
 *
 * @author mical
 * @since 2024/2/18 10:15
 */
data class Limitations(
    private val belonging: AiyatsbusEnchantment,
    private val lines: List<String>
) {

    // 与任何附魔都冲突
    var conflictsWithEverything: Boolean = false

    val limitations = lines.mapNotNull {
        val type = LimitType.valueOf(it.split(":")[0])
        val value = it.split(":").drop(1).joinToString(":")
        if (type == CONFLICT_ENCHANT) {
            if (value == "*") {
                conflictsWithEverything = true
            } else {
                conflicts[belonging.basicData.name] = value
            }
            null
        } else type to value
    }.toMutableList()

    init {
        limitations += MAX_CAPABILITY to ""
        limitations += TARGET to ""
        limitations += DISABLE_WORLD to ""
        limitations += SLOT to ""
    }

    /**
     * 检查操作是否被允许（比如是否可以附魔到某个物品上、使用时是否可以生效、村民生成新交易等）
     * item 就是跟操作直接有关的物品（如正在被附魔的书、正在使用的剑、生成的新交易中卖出的附魔书等）
     *
     * @param ignoreSlot 像烙印诅咒这些，不需要检查装备槽
     */
    fun checkAvailable(
        checkType: CheckType,
        item: ItemStack,
        creature: LivingEntity? = null,
        slot: EquipmentSlot? = null,
        ignoreSlot: Boolean = false
    ): CheckResult {
        return checkAvailable(checkType.limitTypes, item, checkType == CheckType.USE, creature, slot, ignoreSlot)
    }

    fun checkAvailable(
        limits: Collection<LimitType>,
        item: ItemStack,
        use: Boolean = false,
        creature: LivingEntity? = null,
        slot: EquipmentSlot? = null,
        ignoreSlot: Boolean = false
    ): CheckResult {
        // 获取语言
        val sender = creature as? Player ?: Bukkit.getConsoleSender()

        if (!belonging.basicData.enable) {
            return CheckResult.Failed(sender.asLang("limitations-not-enable"))
        }

        limitations.filter { it.first in limits }.forEach { (type, value) ->
            val result = when (type) {
                PAPI_EXPRESSION -> if (creature !is Player) true else value.replacePlaceholder(creature).compileToJexl().eval().coerceBoolean()
                PERMISSION -> creature?.hasPermission(value) ?: true
                DISABLE_WORLD -> creature?.world?.name !in belonging.basicData.disableWorlds
                else -> checkItem(type, item, value, slot, use, ignoreSlot)
            }
            if (!result) {
                return CheckResult.Failed(
                    sender.asLang(
                        "limitations-check-failed",
                        sender.asLang("limitations-typename-${type.name.lowercase()}") to "typename"
                    )
                )
            }
        }

        return CheckResult.Successful
    }

    private fun checkItem(type: LimitType, item: ItemStack, value: String, slot: EquipmentSlot?, use: Boolean, ignoreSlot: Boolean): Boolean {
        val itemType = item.type
        val enchants = item.fixedEnchants
        return when (type) {
            SLOT -> if (slot == null) ignoreSlot else belonging.targets.find { itemType.isInTarget(it) }?.activeSlots?.contains(slot) ?: false
            TARGET -> belonging.targets.any { itemType.isInTarget(it) } || (!use && (itemType == Material.BOOK || itemType == Material.ENCHANTED_BOOK))
            MAX_CAPABILITY -> itemType.capability > enchants.size
            DEPENDENCE_ENCHANT -> return enchants.containsKey(aiyatsbusEt(value))
            CONFLICT_ENCHANT -> return !enchants.containsKey(aiyatsbusEt(value))

            DEPENDENCE_GROUP -> enchants.any { (enchant, _) -> enchant.enchantment.isInGroup(value) && enchant.enchantmentKey != belonging.enchantmentKey }
            CONFLICT_GROUP -> enchants.count { (enchant, _) -> enchant.enchantment.isInGroup(value) && enchant.enchantmentKey != belonging.enchantmentKey } < (aiyatsbusGroup(value)?.maxCoexist ?: 10000)
            else -> true
        }
    }

    fun conflictsWith(other: Enchantment): Boolean {
        return (conflictsWithEverything || other.aiyatsbusEt.limitations.conflictsWithEverything) || limitations.filter { it.first == CONFLICT_ENCHANT || it.first == CONFLICT_GROUP }
            .map {
                when (it.first) {
                    CONFLICT_ENCHANT -> other.key == aiyatsbusEt(it.second)?.enchantmentKey
                    CONFLICT_GROUP -> other.isInGroup(it.second)
                    else -> false
                }
            }.any { true }
    }

    companion object {

        /**
         * 记录单项 conflicts，然后自动挂双向
         * 防止服主只写了单项
         */
        private val conflicts = mutableMapOf<String, String>()

        /**
         * 记录单项 conflicts，然后自动挂双向
         * 防止服主只写了单项
         */
        @Reloadable
        @AwakePriority(LifeCycle.ENABLE, StandardPriorities.LIMITATIONS)
        fun onEnable() {
            conflicts.forEach { (a, b) ->
                val etA = aiyatsbusEt(a) ?: return@forEach
                val etB = aiyatsbusEt(b) ?: return@forEach
                val conflictA = CONFLICT_ENCHANT to b
                val conflictB = CONFLICT_ENCHANT to a
                etA.limitations.limitations.add(conflictA)
                etB.limitations.limitations.add(conflictB)
            }
            conflicts.clear()
        }
    }
}

sealed class CheckResult(val isSuccess: Boolean, val reason: String) {

    val isFailure: Boolean = !isSuccess

    object Successful : CheckResult(true, "")

    class Failed(reason: String) : CheckResult(false, reason)
}

enum class CheckType(vararg types: LimitType) {

    ATTAIN(
        CONFLICT_GROUP,
        CONFLICT_ENCHANT,
        DEPENDENCE_GROUP,
        DEPENDENCE_ENCHANT,
        MAX_CAPABILITY,
        TARGET,
    ), // 从战利品/附魔台中获得附魔物品时
    MERCHANT(
        CONFLICT_GROUP,
        CONFLICT_ENCHANT,
        DEPENDENCE_GROUP,
        DEPENDENCE_ENCHANT,
        MAX_CAPABILITY,
        TARGET
    ), // 生成村民交易中的附魔时
    ANVIL(
        CONFLICT_GROUP,
        CONFLICT_ENCHANT,
        DEPENDENCE_GROUP,
        DEPENDENCE_ENCHANT,
        MAX_CAPABILITY,
        TARGET
    ), // 进行铁砧拼合物品附魔时
    USE(
        PAPI_EXPRESSION,
        PERMISSION,
        DISABLE_WORLD,
        TARGET,
        SLOT
    ); // 使用物品上的附魔时

    val limitTypes = setOf(*types)
}

enum class LimitType {

    TARGET, // 如 锋利只能应用于斧、剑
    MAX_CAPABILITY, // 如 一把剑最多有12附魔（默认）
    PAPI_EXPRESSION, // 如 %player_level%>=30
    PERMISSION, // 如 aiyatsbus.use
    CONFLICT_ENCHANT, // 如 锋利
    CONFLICT_GROUP, // 如 "PVE类附魔"
    DEPENDENCE_ENCHANT, // 如 无限
    DEPENDENCE_GROUP, // 如 "保护类附魔"
    DISABLE_WORLD, // 如 world_the_end
    SLOT // 如 HAND
}