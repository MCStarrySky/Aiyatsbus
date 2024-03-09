package com.mcstarrysky.aiyatsbus.core

import org.bukkit.entity.Player
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
    fun invoke(source: String, player: Player?): CompletableFuture<Any?>
}