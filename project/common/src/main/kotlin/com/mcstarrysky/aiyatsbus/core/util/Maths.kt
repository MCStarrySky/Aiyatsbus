package com.mcstarrysky.aiyatsbus.core.util

import redempt.crunch.CompiledExpression
import redempt.crunch.Crunch
import redempt.crunch.functional.EvaluationEnvironment
import redempt.crunch.functional.Function
import taboolib.common.env.RuntimeDependency
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

@RuntimeDependency(
    value = "com.github.Redempt:Crunch:1.0",
    test = "redempt.crunch.CompiledExpression",
    repository = "http://mcstarrysky.com:8081/repository/releases/"
)
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
        val compiled = Crunch.compileExpression(expression, env)

        cache[expression] = compiled

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
            runCatching { Crunch.compileExpression(expression, env) }.getOrNull()
        }

        return runCatching { compiled?.evaluate(*values) }.getOrNull() ?: 0.0
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