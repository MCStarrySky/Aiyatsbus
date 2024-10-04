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
package com.mcstarrysky.aiyatsbus.core.util

import redempt.crunch.CompiledExpression
import redempt.crunch.Crunch
import redempt.crunch.functional.EvaluationEnvironment
import redempt.crunch.functional.Function
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.function.warning
import taboolib.common.util.random
import kotlin.math.max
import kotlin.math.min

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.MathUtils
 *
 * @author HamsterYDS
 * @since 2024/2/17 23:15
 */
private val romanUnits = intArrayOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
private val romanSymbols = arrayOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")

/**
 * 将数字转化为罗马数字
 */
fun Int.roman(simplified: Boolean = false, blank: Boolean = false): String {
    if ((this == 1 && simplified) || this !in 1..3999) return ""
    var number = this
    val roman = StringBuilder()
    for (i in romanUnits.indices)
        while (number >= romanUnits[i]) {
            roman.append(romanSymbols[i])
            number -= romanUnits[i]
        }
    return if (blank) " $roman" else "$roman"
}

/**
 * 判断一个浮点型是否为整数
 * 比如 6.0 就是整数
 */
fun Double.isInteger(): Boolean {
    return this == toInt().toDouble()
}

object MathUtils {

    private val min = Function("min", 2) {
        min(it[0], it[1])
    }

    private val max = Function("max", 2) {
        max(it[0], it[1])
    }

    private val rand = Function("random", 2) {
        random(it[0], it[1])
    }

    private val cache = mutableMapOf<String, CompiledExpression?>()

    fun String.preheatExpression(): String {
        val expression = replaceVariable()
        val variables = extractVariableNames()

        val env = EvaluationEnvironment()
        env.setVariableNames(*variables.toTypedArray())
        env.addFunctions(rand, min, max)
        val compiled = runCatching { Crunch.compileExpression(expression, env) }.getOrElse {
            error("compiling", this, variables, doubleArrayOf(), it)
            null
        }

        if (compiled != null) {
            cache[expression] = compiled
        }

        return this
    }

    fun String.calculate(holders: List<Pair<String, Any>>): Double {
        val expression = replaceVariable()
        val variables = extractVariableNames()

        val pairMap = holders.toMap()
        val values = variables.map { pairMap[it] }
            .map { it.toString().toDoubleOrNull() ?: 0.0 }
            .toDoubleArray()

        val compiled = cache.getOrPut(expression) {
            val env = EvaluationEnvironment()
            env.setVariableNames(*variables.toTypedArray())
            env.addFunctions(rand, min, max)
            runCatching { Crunch.compileExpression(expression, env) }.getOrElse {
                error("compiling", this, variables, values, it)
                null
            }
        }

        return runCatching { compiled?.evaluate(*values) }.getOrElse {
            error("evaluating", this, variables, values, it)
            0.0
        } ?: 0.0
    }

    private fun error(action: String, expression: String, variables: List<String>, values: DoubleArray, error: Throwable) {
        warning("Error occurred while $action expression!")
        warning("|- Expression: $expression")
        warning("|- Variables: $variables")
        warning("|- Values: ${values.toList()}")
        warning("|- Error: $error")
    }

    private val variableRegex = "\\{([^}]+)}".toRegex()

    /**
     * 提取出公式里的变量
     */
    private fun String.extractVariableNames(): List<String> {
        return (variableRegex.findAll(this).map { it.groupValues[1] }.toList())
    }

    /**
     * 去掉变量两边的大括号
     */
    private fun String.replaceVariable(): String = replace(variableRegex, "\$1")
}