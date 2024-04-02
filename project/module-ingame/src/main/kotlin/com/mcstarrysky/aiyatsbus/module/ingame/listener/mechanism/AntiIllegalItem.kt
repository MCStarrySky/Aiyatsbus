package com.mcstarrysky.aiyatsbus.module.ingame.listener.mechanism

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.util.Reloadable
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import taboolib.module.nms.getI18nName
import taboolib.platform.util.giveItem
import taboolib.platform.util.onlinePlayers

object AntiIllegalItem {

    var task: PlatformExecutor.PlatformTask? = null

    @Reloadable
    @Awake(LifeCycle.CONST)
    fun load() {
        registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.ANTI_ILLEGAL_ITEM) {
            task?.cancel()

            if (!AiyatsbusSettings.enableAntiIllegalItem)
                return@registerLifeCycleTask

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
                            if (!result.first) {
                                enchants.removeAt(j)
                                player.giveItem(et.book(level))
                                item.removeEt(et)
                                player.sendLang(
                                    "info-illegal_item",
                                    item.getI18nName() to "item",
                                    result.second to "reason",
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
}