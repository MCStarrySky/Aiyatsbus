/*
 * This file is part of Vulpecula, licensed under the MIT License.
 *
 *  Copyright (c) 2018 Bkm016
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.mcstarrysky.aiyatsbus.module.kether.util

import com.mcstarrysky.aiyatsbus.module.kether.action.ActionBlock
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.library.kether.*
import taboolib.module.kether.*

/**
 * Vulpecula
 * top.lanscarlos.vulpecula
 *
 * @author Lanscarlos
 * @since 2022-02-27 10:48
 */

/**
 * 查看下一个 Token 但不改变位置
 * */
fun QuestReader.nextPeek(): String {
    this.mark()
    val token = this.nextToken()
    this.reset()
    return token
}

/**
 * 判断是否有指定 token
 * */
fun QuestReader.hasNextToken(vararg expected: String): Boolean {
    if (expected.isEmpty()) return false
    this.mark()
    return if (this.nextToken() in expected) {
        true
    } else {
        this.reset()
        false
    }
}

/**
 * 尝试通过前缀获取 Token
 * */
fun QuestReader.tryNextToken(vararg expected: String): String? {
    return if (this.hasNextToken(*expected)) this.nextToken() else null
}

/**
 * 尝试通过前缀解析 Action
 * */
fun QuestReader.tryNextAction(vararg expected: String): ParsedAction<*>? {
    return if (this.hasNextToken(*expected)) this.nextParsedAction() else null
}

/**
 * 尝试通过前缀解析 Action List
 * */
fun QuestReader.tryNextActionList(vararg expected: String): List<ParsedAction<*>>? {
    return if (this.hasNextToken(*expected)) this.next(ArgTypes.listOf(ArgTypes.ACTION)) else null
}

/**
 * 通过兼容模式解析语句块
 * */
fun QuestReader.nextBlock(): ParsedAction<*> {
    return if (this.hasNextToken("{")) {
        val actions = mutableListOf<ParsedAction<*>>()
        while (!this.hasNextToken("}")) {
            actions += this.nextParsedAction()
        }
        ParsedAction(ActionBlock(actions))
    } else {
        this.nextParsedAction()
    }
}

/**
 * 尝试通过前缀解析语句块
 * */
fun QuestReader.tryNextBlock(vararg expected: String): ParsedAction<*>? {
    return if (this.hasNextToken(*expected)) this.nextBlock() else null
}

/**
 * 获取变量
 * */
fun <T> ScriptFrame.getVariable(key: String): T? {
    val result = variables().get<T>(key)
    return if (result.isPresent) result.get() else null
}

/**
 * 获取变量
 * */
fun <T> ScriptFrame.getVariable(vararg keys: String): T? {
    keys.forEach { key ->
        val result = variables().get<T>(key)
        if (result.isPresent) {
            return result.get()
        }
    }
    return null
}

/**
 * 设置变量
 * */
fun ScriptFrame.setVariable(key: String, value: Any?, deep: Boolean = true) {
    if (deep) {
        var root = this
        while (root.parent().isPresent) {
            root = root.parent().get()
        }
        root.variables().set(key, value)
    } else {
        this.variables().set(key, value)
    }
}

/**
 * 设置变量
 * */
fun ScriptFrame.setVariables(vararg key: String, value: Any?, deep: Boolean = true) {
    key.forEach {
        setVariable(it, value, deep)
    }
}

/**
 * 设置变量
 * */
fun ScriptContext.setVariable(vararg keys: String, value: Any?) {
    keys.forEach { key ->
        set(key, value)
    }
}

fun QuestContext.Frame.playerOrNull(): ProxyPlayer? {
    return script().sender as? ProxyPlayer
}

fun ProxyPlayer.toBukkit(): Player? {
    return this.castSafely()
}