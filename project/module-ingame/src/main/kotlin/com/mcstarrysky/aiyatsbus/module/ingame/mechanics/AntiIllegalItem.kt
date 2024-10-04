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
package com.mcstarrysky.aiyatsbus.module.ingame.mechanics

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.util.inject.Reloadable
import com.mcstarrysky.aiyatsbus.core.util.inject.AwakePriority
import taboolib.common.LifeCycle
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import taboolib.module.nms.getI18nName
import taboolib.platform.util.giveItem
import taboolib.platform.util.onlinePlayers

object AntiIllegalItem {

    var task: PlatformExecutor.PlatformTask? = null

    @Reloadable
    @AwakePriority(LifeCycle.ENABLE, StandardPriorities.ANTI_ILLEGAL_ITEM)
    fun load() {
        task?.cancel()

        if (!AiyatsbusSettings.enableAntiIllegalItem)
            return

        task = submit(period = AiyatsbusSettings.antiIllegalItemInterval) {
            onlinePlayers.forEach { player ->
                val inv = player.inventory
                for (i in 0 until inv.size) {
                    val item = inv.getItem(i) ?: continue
                    val enchants = item.fixedEnchants.toList().toMutableList()
                    if (enchants.isEmpty()) continue
                    var j = 0
                    while (j < enchants.size) {
                        val tmp = item.clone()
                        val et = enchants[j].first
                        val level = enchants[j].second
                        tmp.removeEt(et)
                        val result = et.limitations.checkAvailable(AiyatsbusSettings.antiIllegalItemCheckList, tmp)
                        if (result.isFailure) {
                            enchants.removeAt(j)
                            player.giveItem(et.book(level))
                            item.removeEt(et)
                            player.sendLang(
                                "info-illegal_item",
                                item.getI18nName() to "item",
                                result.reason to "reason",
                                et.displayName() to "enchant"
                            )
                        }
                        j++
                    }
                }
            }
        }
    }
}