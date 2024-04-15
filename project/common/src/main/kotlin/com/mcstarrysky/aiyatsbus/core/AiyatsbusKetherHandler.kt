package com.mcstarrysky.aiyatsbus.core

import org.bukkit.command.CommandSender
import java.util.concurrent.CompletableFuture

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusKetherHandler
 *
 * @author mical
 * @since 2024/3/9 18:25
 */
interface AiyatsbusKetherHandler {

    /**
     * 执行 Kether 脚本
     */
    fun invoke(source: String, sender: CommandSender?, variables: Map<String, Any?> = emptyMap()): CompletableFuture<Any?>?

    /**
     * 预热 Kether 脚本, 提升第一次运行速度
     */
    fun preheat(source: String)
}