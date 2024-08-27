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

fun Collection<String>.variables(reader: VariableReader = VariableReaders.BRACES, func: VariableFunction): List<String> {
    return flatMap { context ->
        val result = ArrayList<String>()
        val queued = HashMap<String, Queue<String>>()
        reader.replaceNested(context) scan@{
            queued[this] = LinkedList(func.transfer(this) ?: return@scan this)
            this
        }
        if (queued.isEmpty()) {
            return@flatMap listOf(context)
        }

        while (queued.any { (_, queue) -> queue.isNotEmpty() }) {
            result += reader.replaceNested(context) {
                if (this in queued) {
                    queued[this]!!.poll() ?: ""
                } else {
                    this
                }
            }
        }
        result
    }
}

fun Collection<String>.variable(key: String, value: Collection<String>, reader: VariableReader = VariableReaders.BRACES): List<String> {
    return variables(reader) { if (it == key) value else null }
}

fun String.singletons(reader: VariableReader = VariableReaders.BRACES, func: Function2<String, String>): String {
    return reader.replaceNested(this) { func.apply(this) }
}

fun Collection<String>.singletons(reader: VariableReader = VariableReaders.BRACES, func: SingleVariableFunction): List<String> {
    return variables(reader, func)
}

fun Collection<String>.singleton(key: String, value: String, reader: VariableReader = VariableReaders.BRACES): List<String> {
    return singletons(reader) { if (it == key) value else null }
}