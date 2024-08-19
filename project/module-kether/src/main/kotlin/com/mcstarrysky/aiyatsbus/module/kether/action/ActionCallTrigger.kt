package com.mcstarrysky.aiyatsbus.module.kether.action

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.data.registry.InternalTriggerLoader
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.module.kether.script

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionCallTrigger
 *
 * @author mical
 * @since 2024/8/19 08:09
 */
object ActionCallTrigger {

    @KetherParser(["call-trigger"], shared = false)
    fun callTriggerParser() = combinationParser {
        it.group(text()).apply(it) { trigger ->
            now {
                val internalTrigger = InternalTriggerLoader.registered[trigger] ?: return@now
                Aiyatsbus.api().getKetherHandler().invoke(
                    internalTrigger.script,
                    script().sender?.castSafely(),
                    variables().toMap()
                )
            }
        }
    }
}