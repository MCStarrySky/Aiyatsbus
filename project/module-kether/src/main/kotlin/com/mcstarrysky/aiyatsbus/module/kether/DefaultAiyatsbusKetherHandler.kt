package com.mcstarrysky.aiyatsbus.module.kether

import com.mcstarrysky.aiyatsbus.core.AiyatsbusKetherHandler
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.console
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions
import taboolib.module.kether.runKether
import java.util.concurrent.CompletableFuture

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.DefaultAiyatsbusKetherHandler
 *
 * @author mical
 * @since 2024/3/9 18:30
 */
class DefaultAiyatsbusKetherHandler : AiyatsbusKetherHandler {

    override fun invoke(source: String, player: Player?, variables: Map<String, Any?>): CompletableFuture<Any?> {
        return runKether(detailError = true) {
            KetherShell.eval(source,
                ScriptOptions.builder().namespace(namespace = listOf("aiyatsbus"))
                    .sender(sender = if (player != null) adaptPlayer(player) else console())
                    .vars(variables)
                    .build())
        }!!
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusKetherHandler>(DefaultAiyatsbusKetherHandler())
        }
    }
}