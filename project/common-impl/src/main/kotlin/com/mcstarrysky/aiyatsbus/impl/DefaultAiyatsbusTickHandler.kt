package com.mcstarrysky.aiyatsbus.impl

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.data.CheckType
import com.mcstarrysky.aiyatsbus.core.util.Mirror
import com.mcstarrysky.aiyatsbus.core.util.Reloadable
import com.mcstarrysky.aiyatsbus.core.util.isNull
import com.mcstarrysky.aiyatsbus.core.util.mirrorNow
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import taboolib.platform.util.onlinePlayers
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.DefaultAiyatsbusTickHandler
 *
 * @author mical
 * @since 2024/3/20 22:10
 */
class DefaultAiyatsbusTickHandler : AiyatsbusTickHandler {

    private var counter = 0
    private var task: PlatformExecutor.PlatformTask? = null
    private val routine: Table<AiyatsbusEnchantment, String, Long> = HashBasedTable.create()

    private val recorder = ConcurrentHashMap<UUID, MutableSet<String>>()

    override fun getRoutine(): Table<AiyatsbusEnchantment, String, Long> {
        return routine
    }

    override fun reset() {
        counter = 0
        task?.cancel()
        task = null
        routine.clear() // 清空等待重新加载
    }

    override fun start() {
        if (task != null) reset()

        task = submit(period = 1L) {
            onTick()
        }
    }

    private fun onTick() {
        counter++
        routine.cellSet().filter { counter % it.value == 0L }.forEach {
            val ench = it.rowKey
            val id = it.columnKey
            val slots = ench.targets.flatMap { it.activeSlots }.toSet()

            onlinePlayers.forEach { player ->
                var flag = false
                val record = recorder.computeIfAbsent(player.uniqueId) { mutableSetOf() }

                // 一般能存在 routine 里的, tickers 必不为 null
                val ticker = ench.trigger.tickers[id] ?: error("Unknown ticker $id for enchantment ${ench.basicData.id}")

                val variables = mutableMapOf(
                    "player" to player,
                    "enchant" to ench,
                    "mirror" to Mirror.MirrorStatus()
                )

                slots.forEach slot@{ slot ->
                    val item: ItemStack
                    try {
                        item = player.inventory.getItem(slot)
                    } catch (_: Throwable) {
                        // 离谱的低版本报错:
                        // java.lang.NullPointerException: player.inventory.getItem(slot) must not be null
                        return@slot
                    }
                    if (item.isNull) return@slot

                    val level = item.etLevel(ench)

                    if (level > 0) {
                        if (!ench.limitations.checkAvailable(CheckType.USE, item, player, slot).first) return@slot
                        flag = true

                        val vars = variables.toMutableMap()
                        vars += mapOf(
                            "triggerSlot" to slot.name,
                            "trigger-slot" to slot.name,
                            "item" to item,
                            "level" to level,
                        )

                        vars += ench.variables.variables(level, player, item, false)

                        if (!record.contains(id)) {
                            record += id
                            if (AiyatsbusSettings.enablePerformanceTool) {
                                mirrorNow("Enchantment:Tick:PreHandle:Kether" + if (AiyatsbusSettings.showPerformanceDetails) ":${ench.basicData.id}" else "") {
                                    vars += "mirror" to it
                                    Aiyatsbus.api().getKetherHandler().invoke(ticker.preHandle, player, vars)
                                }
                            } else {
                                Aiyatsbus.api().getKetherHandler().invoke(ticker.preHandle, player, vars)
                            }
                        }

                        if (AiyatsbusSettings.enablePerformanceTool) {
                            mirrorNow("Enchantment:Tick:Handle:Kether" + if (AiyatsbusSettings.showPerformanceDetails) ":${ench.basicData.id}" else "") {
                                vars += "mirror" to it
                                Aiyatsbus.api().getKetherHandler().invoke(ticker.handle, player, vars)
                            }
                        } else {
                            Aiyatsbus.api().getKetherHandler().invoke(ticker.handle, player, vars)
                        }
                    }
                }
                if (!flag && record.contains(id)) {
                    record -= id
                    if (AiyatsbusSettings.enablePerformanceTool) {
                        mirrorNow("Enchantment:Tick:PostHandle:Kether" + if (AiyatsbusSettings.showPerformanceDetails) ":${ench.basicData.id}" else "") {
                            variables += "mirror" to it
                            Aiyatsbus.api().getKetherHandler().invoke(ticker.postHandle, player, variables)
                        }
                    } else {
                        Aiyatsbus.api().getKetherHandler().invoke(ticker.postHandle, player, variables)
                    }
                }
            }
        }
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusTickHandler>(DefaultAiyatsbusTickHandler())
        }

        @Reloadable
        @Awake(LifeCycle.CONST)
        fun load() {
            registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.TICKERS) {
                Aiyatsbus.api().getTickHandler().reset()
                Aiyatsbus.api().getTickHandler().start()
            }
        }

        @Awake(LifeCycle.DISABLE)
        fun unload() {
            Aiyatsbus.api().getTickHandler().reset()
        }
    }
}