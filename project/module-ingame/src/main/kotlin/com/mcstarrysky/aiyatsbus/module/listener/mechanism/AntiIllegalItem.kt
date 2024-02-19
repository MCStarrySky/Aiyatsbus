package com.mcstarrysky.aiyatsbus.module.listener.mechanism

import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import com.mcstarrysky.aiyatsbus.core.book
import com.mcstarrysky.aiyatsbus.core.data.LimitType
import com.mcstarrysky.aiyatsbus.core.fixedEnchants
import com.mcstarrysky.aiyatsbus.core.removeEt
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import taboolib.module.nms.getI18nName
import taboolib.platform.util.giveItem
import taboolib.platform.util.onlinePlayers
import taboolib.platform.util.sendLang

object AntiIllegalItem {

    var checkList = listOf<LimitType>()
    var task: PlatformExecutor.PlatformTask? = null

    @Awake(LifeCycle.CONST)
    fun load() {
        registerLifeCycleTask(LifeCycle.ENABLE, 7) {
            checkList += AiyatsbusSettings.antiIllegalItemCheckList.map(LimitType::valueOf)

            if (!AiyatsbusSettings.enableAntiIllegalItem)
                return@registerLifeCycleTask

            task?.cancel()
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
                            val result = et.limitations.checkAvailable(checkList, tmp)
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