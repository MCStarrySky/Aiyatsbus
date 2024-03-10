package com.mcstarrysky.aiyatsbus.module.kether.action

import com.mcstarrysky.aiyatsbus.module.kether.*
import com.mcstarrysky.aiyatsbus.module.kether.aiyatsbus
import taboolib.common5.cbool
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.ScriptFrame
import taboolib.module.kether.action.transform.CheckType
import taboolib.module.kether.run
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action
 *
 * @author Lanscarlos
 * @since 2023-03-29 19:01
 */
object ActionIfElse {

    /**
     * 拓展 If 语句
     *
     * if* {...} {...} else {...}
     * if* {...} then {...} else {...}
     * if* {...} {symbol} {...} then {...}
     * if* {...} and/or {...} then {...}
     * */
    @AiyatsbusParser(["if*"])
    fun parser() = aiyatsbus {
        combineOf(
            LiveData {
                val expression = buildExpression(parseExpression(reader = this))
                AiyatsbusKether.Action { frame ->
                    evaluate(frame, expression)
                }
            },
            trim("then", then = action()),
            optional("else", then = action())
        ) { result, pass, deny ->
            if (result) {
                this.run(pass)
            } else if (deny != null) {
                this.run(deny)
            } else {
                CompletableFuture.completedFuture(null)
            }
        }
    }

    /**
     * 构建二叉表达式树
     * @param expression 逆波兰表达式
     * @return 二叉表达式树
     * */
    private fun buildExpression(expression: List<Any>): Expression {
        val stack = Stack<Expression>()
        expression.forEach {
            when (it) {
                is Expression -> {
                    stack.push(it)
                }
                LogicSymbol.AND -> {
                    val left = stack.pop()
                    val right = stack.pop()
                    stack.push(ExpressionNode(true, left, right))
                }
                LogicSymbol.OR -> {
                    val left = stack.pop()
                    val right = stack.pop()
                    stack.push(ExpressionNode(false, left, right))
                }
            }
        }
        return stack.pop()
    }

    /**
     * 解析为逆波兰表达式
     * @param reader 读取器
     * @return 逆波兰表达式
     * */
    private fun parseExpression(reader: AiyatsbusReader): List<Any> {
        val expression = ArrayList<Any>()
        val stack = Stack<LogicSymbol>()

        reader.mark()
        var symbol = reader.nextToken().toSymbol()
            ?: let {
                // 非逻辑符号，先加载条件
                reader.reset()
                expression.add(nextExpressionAction(reader))
                reader.mark()
                reader.nextToken().toSymbol()
            }

        while (symbol != null) {
            when (symbol) {
                // 左括号，入栈
                LogicSymbol.BRACKETS_LEFT -> {
                    stack.push(symbol)
                    // 尝试存入下一个元素
                    if (reader.peekToken() !in logicSymbols) {
                        expression.add(nextExpressionAction(reader))
                    }
                }
                // 右括号
                LogicSymbol.BRACKETS_RIGHT -> {
                    // 栈不为空，符号出栈，加入表达式
                    while (stack.isNotEmpty() && stack.peek() != LogicSymbol.BRACKETS_LEFT) {
                        expression.add(stack.pop())
                    }
                    // 退出左括号
                    stack.pop()
                }
                else -> {
                    if (stack.empty() || stack.peek() == LogicSymbol.BRACKETS_LEFT || symbol.priority > stack.peek().priority) {
                        // 栈空 或 栈顶为左括号 或 当前运算符优先级高
                        // 运算符入栈
                        stack.push(symbol)
                        // 尝试存入下一个元素
                        if (reader.peekToken() !in logicSymbols) {
                            expression.add(nextExpressionAction(reader))
                        }
                    } else {
                        // 当前符号优先级低或同级
                        while (stack.isNotEmpty() && stack.peek().priority >= symbol.priority) {
                            // 将栈内运算符依次取出，直至栈空 或 栈顶运算符优先级低于当前运算符
                            expression.add(stack.pop())
                        }
                        stack.push(symbol)
                        // 尝试存入下一个元素
                        if (reader.peekToken() !in logicSymbols) {
                            expression.add(nextExpressionAction(reader))
                        }
                    }
                }
            }
            reader.mark()
            symbol = reader.nextToken().toSymbol()
        }

        reader.reset()

        // 将栈内剩余符号取出
        while (stack.isNotEmpty()) {
            expression.add(stack.pop())
        }

        return expression
    }

    private val checkSymbols = setOf(
        "==", "is",
        "!=", "!is", "not",
        "=!", "is!",
        "=!!", "is!!",
        "=?", "is?",
        ">", "gt",
        ">=", "gte",
        "<", "lt",
        "<=", "lte",
        "in",
        "contains", "has"
    )

    private val logicSymbols = setOf(
        "(", ")",
        "&&", "and",
        "||", "or"
    )

    /**
     * 逻辑表达式
     * */
    interface Expression

    /**
     * 逻辑表达式节点
     * */
    class ExpressionNode(
        val logic: Boolean, // true 为与，false 为或
        val left: Expression? = null,
        val right: Expression? = null
    ) : Expression

    /**
     * 逻辑表达式动作
     * */
    open class ExpressionAction(
        val action: ParsedAction<*>
    ) : Expression {
        open fun run(frame: ScriptFrame): CompletableFuture<Boolean> {
            return frame.run(action).thenApply { it?.cbool ?: false }
        }
    }

    /**
     * 逻辑表达式动作 Check
     * */
    class ExpressionActionCheck(
        action: ParsedAction<*>,
        val other: ParsedAction<*>,
        val checkType: CheckType
    ) : ExpressionAction(action) {
        override fun run(frame: ScriptFrame): CompletableFuture<Boolean> {
            return applicative(frame.run(action), frame.run(other)).thenApply {
                checkType.check(it.t1, it.t2)
            }
        }
    }

    /**
     * 获取下一个可执行的语句
     * */
    private fun nextExpressionAction(reader: AiyatsbusReader): ExpressionAction {
        val action = reader.readAction()
        reader.mark()
        val next = reader.nextToken().lowercase()
        return if (next in checkSymbols) {
            val other = reader.readAction()
            val checkType = CheckType.fromString(next)
            ExpressionActionCheck(action, other, checkType)
        } else {
            reader.reset()
            ExpressionAction(action)
        }
    }

    /**
     * 执行逻辑表达式
     * */
    fun evaluate(frame: ScriptFrame, expression: Expression): CompletableFuture<Boolean> {
        return when (expression) {
            is ExpressionAction -> expression.run(frame)
            is ExpressionNode -> {
                when {
                    expression.left != null && expression.right != null -> {
                        val left = evaluate(frame, expression.left)
                        val right = evaluate(frame, expression.right)
                        if (expression.logic) {
                            applicative(left, right).thenApply { it.t1 && it.t2 }
                        } else {
                            applicative(left, right).thenApply { it.t1 || it.t2 }
                        }
                    }
                    expression.left != null -> evaluate(frame, expression.left)
                    expression.right != null -> evaluate(frame, expression.right)
                    else -> throw IllegalArgumentException()
                }
            }
            else -> throw IllegalArgumentException()
        }
    }

    private fun String.toSymbol(): LogicSymbol? {
        return when (this) {
            "(" -> LogicSymbol.BRACKETS_LEFT
            ")" -> LogicSymbol.BRACKETS_RIGHT
            "&&", "and" -> LogicSymbol.AND
            "||", "or" -> LogicSymbol.OR
            else -> null
        }
    }

    enum class LogicSymbol(val priority: Int) {
        BRACKETS_LEFT(3),
        BRACKETS_RIGHT(3),
        AND(1),
        OR(0);
    }

}