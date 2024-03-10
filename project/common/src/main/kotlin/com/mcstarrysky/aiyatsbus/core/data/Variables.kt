package com.mcstarrysky.aiyatsbus.core.data

import com.mcstarrysky.aiyatsbus.core.util.calculate
import com.mcstarrysky.aiyatsbus.core.util.get
import com.mcstarrysky.aiyatsbus.core.util.set
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import taboolib.common5.cint
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.util.asMap
import taboolib.platform.compat.replacePlaceholder
import taboolib.platform.util.modifyMeta

/**
 * 附魔的变量显示与替换
 *
 * leveled - 与等级有关的变量, 例如等级越高触发概率越大, 公式需要带入等级计算
 * player_related - 依赖玩家的变量, 例如 PlaceholderAPI
 * modifiable - 与物品强相关的数据, 需要写在物品的 PDC 里, 武器击杀次数累积多少触发什么东西
 * ordinary - 普通的变量, 你也可以理解为配置项, 不提供任何计算
 * flexible - 读取代码有关的数据, 与 BukkitAPI 强相关, 这个麻烦, 先不写
 *
 * @author mical
 * @since 2024/2/17 22:29
 */
class Variables(
    /** 存储变量的类型, 要根据这个判断变量类型 */
    val variables: MutableMap<String, VariableType>,
    /** 与等级有关的变量, Pair 里的 String 是单位, Map 里的 Int 是等级, String 是公式 */
    val leveled: MutableMap<String, Pair<String, LinkedHashMap<Int, String>>>,
    /** 依赖玩家的变量, 变量名对 PAPI 变量 */
    val playerRelated: MutableMap<String, String>,
    /** 与物品强相关的数据, 变量名对初始值 */
    val modifiable: MutableMap<String, Pair<String, String>>,
    /** 普通的变量, 变量名对值 */
    val ordinary: MutableMap<String, String>
) {

    fun ordinary(variable: String): String = ordinary[variable]!!

    /**
     * 计算与等级有关的变量并返回结果
     */
    fun leveled(variable: String, level: Int?, withUnit: Boolean): String {
        return level?.let {
            val v = leveled[variable]!!
            v.second
                .filter { it.key <= level }
                .minBy { level - it.key }.value
                .calculate("level" to level) + if (withUnit) v.first else ""
        } ?: "?"
    }

    /**
     * 计算依赖玩家的变量并返回结果
     */
    fun playerRelated(variable: String, player: Player?): String = player?.let { playerRelated[variable]!!.replacePlaceholder(player) } ?: "?"

    /**
     * 计算与物品强相关的变量并返回结果
     */
    fun modifiable(variable: String, item: ItemStack?): String {
        val pair = modifiable[variable]!!
        return item?.let { it.itemMeta["aiyatsbus_" + pair.first, PersistentDataType.STRING] ?: pair.second } ?: "?"
    }

    /**
     * 自动计算所有的变量, 并将所有结果放入 Map
     *
     * @param level 计算与等级有关的变量需要的等级
     * @param entity 计算依赖玩家的变量需要的玩家对象, 后期可能拓展给 flexible 所以并不是 Player 而是 LivingEntity
     * @param item 计算与物品强相关的变量时需要的物品堆
     * @param withUnit 带不带单位, 不写单位这题扣一分
     */
    fun variables(
        level: Int?,
        entity: LivingEntity? = null,
        item: ItemStack? = null,
        withUnit: Boolean = true
    ): Map<String, String> {
        return variables.mapNotNull { (variable, type) ->
            variable to when (type) {
                VariableType.LEVELED -> leveled(variable, level, withUnit)
                VariableType.PLAYER_RELATED -> playerRelated(variable, entity as? Player)
                VariableType.MODIFIABLE -> modifiable(variable, item)
                VariableType.ORDINARY -> ordinary(variable)
            }
        }.toMap()
    }

    /**
     * 修改物品变量
     */
    fun modifyVariable(item: ItemStack, variable: String, value: String): ItemStack = item.modifyMeta<ItemMeta> {
        this["aiyatsbus_" + modifiable[variable]!!.first, PersistentDataType.STRING] = value
    }

    companion object {

        fun load(variableConfig: ConfigurationSection?): Variables {
            return Variables(mutableMapOf(), mutableMapOf(), mutableMapOf(), mutableMapOf(), mutableMapOf()).apply {
                variableConfig?.run {
                    getConfigurationSection("leveled").asMap().forEach { (variable, any) ->
                        if (any is ConfigurationSection) {
                            val map = linkedMapOf<Int, String>()
                            any.asMap().mapKeys { it.key.cint }.mapValues { it.value.toString() }.toSortedMap().forEach {
                                map[it.key] = it.value
                            }
                            leveled[variable] = any.getString("unit", "单位")!! to map
                        } else {
                            val (unit, formula) = any.toString().split(":", limit = 2)
                            leveled[variable] = unit to linkedMapOf(1 to formula)
                        }
                        variables[variable] = VariableType.LEVELED
                    }
                    getConfigurationSection("player_related").asMap().forEach { (variable, expression) ->
                        playerRelated[variable] = expression.toString()
                        variables[variable] = VariableType.PLAYER_RELATED
                    }
                    getConfigurationSection("modifiable").asMap().forEach { (variable, expression) ->
                        val parts = expression.toString().split('=')
                        modifiable[variable] = parts[0] to parts[1]
                        variables[variable] = VariableType.MODIFIABLE
                    }
                    getConfigurationSection("ordinary").asMap().forEach { (variable, value) ->
                        ordinary[variable] = value.toString()
                        variables[variable] = VariableType.ORDINARY
                    }
                }
            }
        }
    }
}

enum class VariableType {

    LEVELED, PLAYER_RELATED, MODIFIABLE, ORDINARY
}