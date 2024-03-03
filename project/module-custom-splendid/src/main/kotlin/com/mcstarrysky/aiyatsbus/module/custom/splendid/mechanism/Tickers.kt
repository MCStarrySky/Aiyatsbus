package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.StandardPriorities
import com.mcstarrysky.aiyatsbus.core.data.CheckType
import com.mcstarrysky.aiyatsbus.core.etLevel
import com.mcstarrysky.aiyatsbus.core.mechanism.Reloadable
import com.mcstarrysky.aiyatsbus.core.util.calcToDouble
import com.mcstarrysky.aiyatsbus.core.util.calcToInt
import com.mcstarrysky.aiyatsbus.core.util.isNull
import com.mcstarrysky.aiyatsbus.module.custom.splendid.SplendidTrigger
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import taboolib.library.configuration.ConfigurationSection
import taboolib.platform.util.onlinePlayers
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.Tickers.Companion.Mode.*
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.chain.Chain
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.chain.ChainType
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.ObjectEntry
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.objPlayer
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.registerLifeCycleTask
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class Tickers(val enchant: AiyatsbusEnchantment, config: ConfigurationSection?) {

    val byId = mutableMapOf<String, List<Chain>>()
    val beforeById = mutableMapOf<String, List<Chain>>()
    val afterById = mutableMapOf<String, List<Chain>>()

    init {
        config?.getKeys(false)?.forEach { id ->
            val interval = config.getInt("$id.interval", 20)
            byId[id] = config.getStringList("$id.chains").map { Chain(enchant, it) }
            beforeById[id] = config.getStringList("$id.chains_before").map { Chain(enchant, it) }
            afterById[id] = config.getStringList("$id.chains_after").map { Chain(enchant, it) }
            routine[enchant to "${enchant.basicData.id}.$id"] = interval
        }
    }

    fun trigger(player: Player, item: ItemStack, mode: Mode) {
        when (mode) {
            BEFORE -> beforeById
            AFTER -> afterById
            NORMAL -> byId
        }.forEach { (_, chains) ->
            val sHolders = mutableMapOf<String, String>()
            val fHolders = mutableMapOf<String, Pair<ObjectEntry<*>, String>>()

            fun next(tot: Int = 0) {
                if (tot < 0) return // FIXME: 意外情况
                if (tot >= chains.size) return
                val chain = chains[tot]
                sHolders["随机数"] = (Math.random() * 100).roundToInt().toString()
                sHolders += enchant.variables.variables(item.etLevel(enchant), player, item, false)
                fHolders["玩家"] = objPlayer.h(player)

                if (chain.type == ChainType.DELAY) submit(delay = (chain.content.calcToDouble(sHolders) * 20).roundToLong()) { next(tot + 1) }
                else if (chain.type == ChainType.GOTO) next(chain.content.calcToInt(sHolders) - 1)
                else if (chain.trigger(null, null, player, item, sHolders, fHolders)) next(tot + 1)
            }
            next()
        }
    }

    companion object {

        val routine = mutableMapOf<Pair<AiyatsbusEnchantment, String>, Int>()

        val recorder = mutableMapOf<UUID, MutableSet<String>>()

        var task: PlatformExecutor.PlatformTask? = null

        enum class Mode {
            BEFORE,
            AFTER,
            NORMAL
        }

        @SubscribeEvent
        fun quit(event: PlayerQuitEvent) {
            recorder.remove(event.player.uniqueId)
        }

        @Reloadable
        @Awake(LifeCycle.CONST)
        fun load() {
            registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.TICKERS) {
                routine.clear()

                var counter = 0
                task?.cancel()
                task = submit(period = 1L) {
                    counter++
                    routine.filterValues { counter % it == 0 }.forEach { (pair, _) ->
                        val enchant = pair.first
                        // 二次判断
                        val trigger: SplendidTrigger = enchant.trigger as? SplendidTrigger ?: return@forEach
                        val id = pair.second
                        val slots = enchant.targets.flatMap { it.activeSlots }.toSet()
                        onlinePlayers.forEach { player ->
                            var flag = false
                            val set = recorder.getOrPut(player.uniqueId) { mutableSetOf() }
                            slots.forEach slot@{ slot ->
                                val item = player.inventory.getItem(slot)
                                if (item.isNull) return@slot
                                if (item!!.etLevel(enchant) > 0) {
                                    if (!enchant.limitations.checkAvailable(CheckType.USE, item, player, slot).first) return@slot
                                    flag = true

                                    if (!set.contains(id)) {
                                        set += pair.second
                                        trigger.tickers.trigger(player, item, BEFORE)

                                    }
                                    trigger.tickers.trigger(player, item, NORMAL)
                                }
                            }
                            if (!flag && set.contains(id)) {
                                set -= pair.second
                                trigger.tickers.trigger(player, ItemStack(Material.STONE), AFTER)
                            }
                        }
                    }
                }
            }
        }
    }
}