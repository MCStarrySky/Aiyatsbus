package com.mcstarrysky.aiyatsbus.impl

import com.mcstarrysky.aiyatsbus.core.AiyatsbusKetherHandler
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.console
import taboolib.module.kether.KetherShell
import java.util.concurrent.CompletableFuture

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.DefaultAiyatsbusKetherHandler
 *
 * @author mical
 * @since 2024/3/9 18:30
 */
class DefaultAiyatsbusKetherHandler : AiyatsbusKetherHandler {

    override fun invoke(source: String, player: Player?): CompletableFuture<Any?> {
        return KetherShell.eval(source, sender = if (player != null) adaptPlayer(player) else console(), namespace = listOf("aiyatsbus"))
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusKetherHandler>(DefaultAiyatsbusKetherHandler())
        }
    }
}