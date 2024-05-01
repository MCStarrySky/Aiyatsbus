package com.mcstarrysky.aiyatsbus.core.util

import taboolib.common5.cdouble
import java.math.BigDecimal
import java.util.*

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

    /**
     * 计算数字
     */
    private fun calculateToDouble(a1: BigDecimal, a2: BigDecimal, operator: Char): Double {
        return when (operator) {
            '+' -> a1 + a2
            '-' -> a1 - a2
            '*' -> a1 * a2
            '/' -> a1 / a2
            else -> throw IllegalArgumentException("Unknown operator: $operator")
        }.toDouble()
    }

    /**
     * 获取优先级
     * 小学问题, 先乘除后加减, 我弟都会
     */
    private fun getPriority(symbol: String?): Int {
        return when (symbol) {
            null -> 0
            "(" -> 1
            "+", "-" -> 2
            "*", "/" -> 3
            else -> throw IllegalArgumentException("Unknown symbol: $symbol")
        }
    }

    /**
     * 将中缀表达式转化为后缀表达式(逆波兰表达式)
     */
    private fun toRpnExpr(expr: String): String {
        val buffer = StringBuffer()
        val operator = Stack<String>()
        operator.push(null)

        val pattern = "(?<!\\d)-?\\d+(\\.\\d+)?|[+\\-*/()]".toPattern()
        val matcher = pattern.matcher(expr)

        while (matcher.find()) {
            val symbol = matcher.group()
            if (symbol.matches("[+\\-*/()]".toRegex())) {
                if (symbol.equals("(")) {
                    operator.push(symbol)
                } else if (symbol.equals(")")) {
                    while (operator.peek() != "(") {
                        buffer.append(operator.pop()).append(" ")
                    }
                    operator.pop()
                } else {
                    while (getPriority(operator.peek()) >= getPriority(symbol)) {
                        buffer.append(operator.pop()).append(" ")
                    }
                    operator.push(symbol)
                }
            } else {
                buffer.append(symbol).append(" ")
            }
        }

        while (operator.peek() != null) {
            buffer.append(operator.pop()).append(" ")
        }
        return buffer.toString()
    }

    /**
     * 计算表达式
     */
    fun calculate(expr: String): Double {
        val rpnExpr = toRpnExpr(expr)
        val pattern = "-?\\d+(\\.\\d+)?|[+\\-*/]".toPattern()
        val matcher = pattern.matcher(rpnExpr)
        val stack = Stack<Double>()

        while (matcher.find()) {
            val symbol = matcher.group()
            if (symbol.matches("[+\\-*/]".toRegex())) {
                val a2 = stack.pop()
                val a1 = stack.pop()
                stack.push(calculateToDouble(a1.toBigDecimal(), a2.toBigDecimal(), symbol[0]))
            } else {
                stack.push(symbol.cdouble)
            }
        }
        return stack.pop()
    }
}