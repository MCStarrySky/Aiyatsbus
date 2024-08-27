@file:Suppress("unused")

package com.mcstarrysky.aiyatsbus.core.util

import taboolib.common.util.VariableReader
import java.util.*

object VariableReaders {
    val BRACES by lazy { VariableReader("{", "}") }
    val DOUBLE_BRACES by lazy { VariableReader("{{", "}}") }
    val PERCENT by lazy { VariableReader("%", "%") }

    internal val AREA_START by lazy { "^#area (?<area>.+)$".toRegex() }
    internal val AREA_END by lazy { "^#end(?: (?<area>.+))?$".toRegex() }
}

fun interface VariableFunction {
    fun transfer(name: String): Collection<String>?
}

fun interface SingleVariableFunction : VariableFunction {
    fun replace(name: String): String?

    override fun transfer(name: String): Collection<String>? = replace(name)?.let(::listOf)
}

fun String.variables(reader: VariableReader = VariableReaders.BRACES, func: VariableFunction): String {
    val queued = HashMap<String, Queue<String>>()
    reader.replaceNested(this) scan@{
        queued[this] = LinkedList(func.transfer(this) ?: return@scan this)
        this
    }

    if (queued.isEmpty()) {
        return this
    }

    val result = StringBuilder(this)
    while (queued.any { (_, queue) -> queue.isNotEmpty() }) {
        result.replace(0, result.length, reader.replaceNested(result.toString()) {
            if (this in queued) {
                queued[this]!!.poll() ?: ""
            } else {
                this
            }
        })
    }
    return result.toString()
}

fun Collection<String>.variables(reader: VariableReader = VariableReaders.BRACES, func: VariableFunction): List<String> {
    return map { it.variables(reader, func) }
}

fun Collection<String>.variable(key: String, value: Collection<String>, reader: VariableReader = VariableReaders.BRACES): List<String> {
    return variables(reader) { if (it == key) value else null }
}

fun String.singletons(reader: VariableReader = VariableReaders.BRACES, func: SingleVariableFunction): String {
    return variables(reader, func)
}

fun Collection<String>.singletons(reader: VariableReader = VariableReaders.BRACES, func: SingleVariableFunction): List<String> {
    return variables(reader, func)
}

fun Collection<String>.singleton(key: String, value: String, reader: VariableReader = VariableReaders.BRACES): List<String> {
    return singletons(reader) { if (it == key) value else null }
}