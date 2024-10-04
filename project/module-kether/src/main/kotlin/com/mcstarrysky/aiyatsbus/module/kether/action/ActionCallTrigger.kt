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