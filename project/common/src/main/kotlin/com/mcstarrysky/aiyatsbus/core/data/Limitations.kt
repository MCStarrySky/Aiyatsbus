package com.mcstarrysky.aiyatsbus.core.data

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.data.LimitType.*
import com.mcstarrysky.aiyatsbus.core.util.Reloadable
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.module.kether.compileToJexl
import taboolib.platform.compat.replacePlaceholder
import taboolib.platform.util.asLangText

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

    val limitations = lines.mapNotNull {
        val type = LimitType.valueOf(it.split(":")[0])
        val value = it.split(":").drop(1).joinToString(":")
        if (type == CONFLICT_ENCHANT) {
            conflicts[belonging.basicData.name] = value
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
     */
    fun checkAvailable(checkType: CheckType, item: ItemStack, creature: LivingEntity? = null, slot: EquipmentSlot? = null): Pair<Boolean, String> {
        return checkAvailable(checkType.limitTypes.toList(), item, creature, slot, checkType == CheckType.USE)
    }

    fun checkAvailable(
        limits: List<LimitType>,
        item: ItemStack,
        creature: LivingEntity? = null,
        slot: EquipmentSlot? = null,
        use: Boolean = false
    ): Pair<Boolean, String> {
        val sender = creature as? Player ?: Bukkit.getConsoleSender()

        if (!belonging.basicData.enable)
            return false to sender.asLangText("limitations-not-enable")

        limitations.filter { limits.contains(it.first) }.forEach { (type, value) ->
            when (type) {
                PAPI_EXPRESSION ->
                    if (creature is Player) value.replacePlaceholder(creature).compileToJexl().eval() as Boolean
                    else true

                PERMISSION -> (creature?.hasPermission(value) ?: true)
                DISABLE_WORLD -> !belonging.basicData.disableWorlds.contains(creature?.world?.name)
                TARGET, MAX_CAPABILITY, SLOT,
                CONFLICT_ENCHANT, CONFLICT_GROUP,
                DEPENDENCE_ENCHANT, DEPENDENCE_GROUP -> checkItem(type, item, value, slot, use)
            }.run { if (!this) return false to sender.asLangText("limitations-check-failed", sender.asLangText("limitations-typename-${type.name.lowercase()}") to "typename") }
        }

        return true to ""
    }

    private fun checkItem(type: LimitType, item: ItemStack, value: String, slot: EquipmentSlot?, use: Boolean): Boolean {
        val itemType = item.type
        val enchants = item.fixedEnchants
        return when (type) {
            SLOT -> belonging.targets.find { itemType.isInTarget(it) }?.activeSlots?.contains(slot) ?: false
            TARGET -> belonging.targets.any { itemType.isInTarget(it) } || (!use && (itemType == Material.BOOK || itemType == Material.ENCHANTED_BOOK))
            MAX_CAPABILITY -> itemType.capability > enchants.size
            DEPENDENCE_ENCHANT -> return enchants.containsKey(aiyatsbusEt(value))
            CONFLICT_ENCHANT -> return !enchants.containsKey(aiyatsbusEt(value))

            DEPENDENCE_GROUP -> enchants.any { (enchant, _) -> enchant.enchantment.isInGroup(value) && enchant.enchantmentKey != belonging.enchantmentKey }
            CONFLICT_GROUP -> enchants.count { (enchant, _) -> enchant.enchantment.isInGroup(value) && enchant.enchantmentKey != belonging.enchantmentKey } < (Group.groups[value]?.maxCoexist ?: 10000)
            else -> true
        }
    }

    companion object {

        /**
         * 记录单项 conflicts，然后自动挂双向
         * 防止服主只写了单项
         */
        val conflicts = mutableMapOf<String, String>()

        /**
         * 记录单项 conflicts，然后自动挂双向
         * 防止服主只写了单项
         */
        @Reloadable
        @Awake(LifeCycle.CONST)
        fun init() {
            registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.LIMITATIONS) {
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

    val limitTypes = mutableSetOf(*types)

    fun has(limitType: LimitType): Boolean = limitTypes.contains(limitType)
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