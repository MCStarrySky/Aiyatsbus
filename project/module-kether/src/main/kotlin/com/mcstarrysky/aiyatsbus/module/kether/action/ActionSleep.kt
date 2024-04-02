package com.mcstarrysky.aiyatsbus.module.kether.action

import taboolib.common.platform.function.isPrimaryThread
import taboolib.common.platform.function.submit
import taboolib.library.kether.ArgTypes
import taboolib.module.kether.*
import kotlin.math.roundToLong

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionSleep
 *
 * @author mical
 * @since 2024/4/1 22:41
 */
object ActionSleep {

    @KetherParser(["a-wait", "a-delay", "a-sleep"])
    fun actionWait() = scriptParser {
        val ticks = it.next(ArgTypes.ACTION)
        actionFuture { f ->
            newFrame(ticks).run<Double>().thenApply { d ->
                val task = submit(delay = (d * 20).roundToLong(), async = !isPrimaryThread) {
                    // 如果玩家在等待过程中离线则终止脚本
                    if (script().sender?.isOnline() == false) {
                        ScriptService.terminateQuest(script())
                        return@submit
                    }
                    f.complete(null)
                }
                addClosable(AutoCloseable { task.cancel() })
            }
        }
    }
}