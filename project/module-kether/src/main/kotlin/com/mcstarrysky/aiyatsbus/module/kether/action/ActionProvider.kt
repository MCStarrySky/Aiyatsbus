package com.mcstarrysky.aiyatsbus.module.kether.action

import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionProvider
 *
 * @author mical
 * @since 2024/3/10 10:41
 */
abstract class ActionProvider<T> {

    /**
     * 读取字段
     */
    abstract fun read(instance: T, key: String): Any?

    /**
     * 写入字段
     */
    abstract fun write(instance: T, key: String, value: Any?): OpenResult
}