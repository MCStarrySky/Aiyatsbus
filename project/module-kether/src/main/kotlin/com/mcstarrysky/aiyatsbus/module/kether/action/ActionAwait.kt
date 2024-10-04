/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.mcstarrysky.aiyatsbus.module.kether.action

import taboolib.common.platform.function.isPrimaryThread
import taboolib.common.platform.function.submit
import taboolib.library.kether.ArgTypes
import taboolib.module.kether.*
import kotlin.math.roundToLong

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionAwait
 *
 * @author mical
 * @since 2024/7/14 12:41
 */
object ActionAwait {

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