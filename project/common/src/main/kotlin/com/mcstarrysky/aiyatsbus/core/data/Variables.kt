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

import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import com.mcstarrysky.aiyatsbus.core.util.*
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import taboolib.common5.cint
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.util.asMap
import taboolib.module.nms.getItemTag
import taboolib.module.nms.setItemTag
import taboolib.platform.util.modifyMeta

/**
 * 附魔的变量显示与替换
 *
 * leveled - 与等级有关的变量, 例如等级越高触发概率越大, 公式需要带入等级计算, 可嵌套其他变量但严禁互相嵌套(否则可能会导致灾难性的后果)
 * modifiable - 与物品强相关的数据, 需要写在物品的 PDC 里, 武器击杀次数累积多少触发什么东西
 * ordinary - 常量, 你也可以理解为配置项, 不提供任何计算
 *
 * @author mical
 * @since 2024/2/17 22:29
 */
enum class VariableType {
    LEVELED, MODIFIABLE, ORDINARY
}

class Variables(
    root: ConfigurationSection?
) {

    /** 存储变量的类型, 要根据这个判断变量类型 */
    private val variables: MutableMap<String, VariableType> = HashMap()

    /** 与等级有关的变量, Pair 里的 String 是单位, Map 里的 Int 是等级, String 是公式 */
    val leveled: MutableMap<String, Pair<String, Map<Int, String>>> = HashMap()

    /** 与物品强相关的数据, 变量名对初始值 */
    private val modifiable: MutableMap<String, Pair<String, String>> = HashMap()

    /** 常量, 相当于附魔配置, 变量名对值 */
    private val ordinary: MutableMap<String, Any?> = HashMap()

    init {
        // leveled
        root?.getConfigurationSection("leveled").asMap().forEach { (variable, section) ->
            // 如果分级配置了不同的值
            if (section is ConfigurationSection) {
                leveled[variable] = (section["unit"]?.toString().orEmpty()) to section.asMap()
                    .map { it.key.cint to it.value.toString() }.toMap()
            } else {
                val (unit, formula) = section.toString().split(":", limit = 2)
                // 只存 1 级, 任何等级都能获取到
                leveled[variable] = unit to mapOf(1 to formula)
            }
            // 存储该变量的类型
            variables[variable] = VariableType.LEVELED
        }
        // modifiable
        root?.getConfigurationSection("modifiable").asMap().forEach { (variable, expression) ->
            val parts = expression.toString().split('=')
            modifiable[variable] = parts[0] to parts[1]
            variables[variable] = VariableType.MODIFIABLE
        }
        // ordinary
        root?.getConfigurationSection("ordinary").asMap().forEach { (variable, value) ->
            ordinary[variable] = value
            variables[variable] = VariableType.ORDINARY
        }
    }

    /**
     * 计算与等级有关的变量并返回结果
     */
    fun leveled(variable: String, level: Int, withUnit: Boolean): Any {
        val v = leveled[variable]!! // 获取变量
        return v.second
            .filter { it.key <= level } // 过滤掉等级高于当前等级的参数
            .minBy { level - it.key }.value // 取当前变量与该变量的差的最小值的变量, 当然也就是最高的那个等级配置
            .singletons(VariableReaders.DOUBLE_BRACES) { leveled(it, level, false).toString() } // 尝试解析其中的嵌套变量(双括号)
            .calcToDouble("level" to level)
            .let {
                // 如果是小数形式的整数则只保留整数位
                if (it.isInteger()) it.toInt() else it.toBigDecimal()
                    .setScale(AiyatsbusSettings.variableRoundingScale, AiyatsbusSettings.variableRoundingMode).toDouble()
            }
            .let { if (withUnit) it.toString() + v.first else it }
    }

    /**
     * 计算物品变量并返回结果
     * 默认是从物品的 PDC 中获取, 如果变量名开头为 (NBT) 则会自动去掉该开头并从物品 NBT 中寻找该变量
     */
    fun modifiable(variable: String, item: ItemStack?): Any {
        if (item == null) return "?"
        val v = modifiable[variable]!!
        val usingNBT = v.first.startsWith("(NBT)")
        if (usingNBT) {
            return item.getItemTag().getDeep(v.first.removePrefix("(NBT)"))?.unsafeData() ?: v.second
        }
        return item.itemMeta[v.first, PersistentDataType.STRING] ?: v.second
    }

    /**
     * 修改物品变量, 可以使用 NBT
     */
    fun modifyVariable(item: ItemStack, variable: String, value: String): ItemStack {
        val v = modifiable[variable]!!
        val usingNBT = v.first.startsWith("(NBT)")
        if (usingNBT) {
            val tag = item.getItemTag()
            tag.putDeep(v.first.removePrefix("(NBT)"), v.second)
            item.setItemTag(tag)
            return item
        }
        return item.modifyMeta<ItemMeta> {
            this[modifiable[variable]!!.first, PersistentDataType.STRING] = value
        }
    }

    /**
     * 计算常量并返回结果
     */
    fun ordinary(variable: String): Any? = ordinary[variable]

    /**
     * 计算变量并得到值
     *
     * @param level 计算与等级有关的变量需要的等级
     * @param item 计算与物品强相关的变量时需要的物品堆
     * @param withUnit 带不带单位, 不写单位这题扣一分
     */
    fun variable(variable: String, level: Int, item: ItemStack? = null, withUnit: Boolean = false): Any? {
        return when (variables[variable]!!) {
            VariableType.LEVELED -> leveled(variable, level, withUnit)
            VariableType.MODIFIABLE -> modifiable(variable, item)
            VariableType.ORDINARY -> ordinary(variable)
        }
    }

    /**
     * 自动计算所有的变量, 并将所有结果放入 Map
     */
    fun variables(level: Int, item: ItemStack? = null, withUnit: Boolean = false): Map<String, Any?> {
        return variables.mapValues { variable(it.key, level, item, withUnit) }
    }
}