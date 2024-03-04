package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.data.CheckType
import com.mcstarrysky.aiyatsbus.core.data.VariableType
import com.mcstarrysky.aiyatsbus.core.etLevel
import com.mcstarrysky.aiyatsbus.core.util.calcToDouble
import com.mcstarrysky.aiyatsbus.core.util.calcToInt
import com.mcstarrysky.aiyatsbus.core.util.replace
import com.mcstarrysky.aiyatsbus.module.custom.splendid.SplendidTrigger
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.submit
import taboolib.library.configuration.ConfigurationSection
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.chain.Chain
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.chain.ChainType
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.ObjectEntry
import taboolib.common5.cbool
import taboolib.common5.cint
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class Listeners(val enchant: AiyatsbusEnchantment, val trigger: SplendidTrigger, config: ConfigurationSection?) {

    //一个listener就是 list<chain>
    //一个listenerId指向一个listener
    val byId = mutableMapOf<String, Pair<EventPriority, List<Chain>>>()

    //一个eventType指向所有该type的listeners的ids
    val byType = mutableMapOf<EventType, MutableList<String>>()

    init {
        config?.getKeys(false)?.forEach { id ->
            val type = EventType.getType(config.getString("$id.type")) ?: return@forEach
            val priority = EventPriority.values().find { it.name == config.getString("$id.priority", "HIGHEST") } ?: return@forEach
            val lines = config.getStringList("$id.chains")
            val chains = lines.map { Chain(enchant, it) }
            byId[id] = priority to chains
            byType.getOrPut(type) { mutableListOf() } += id
        }
    }

    fun trigger(
        event: Event,
        eventType: EventType,
        priority: EventPriority,
        entity: LivingEntity,
        item: ItemStack,
        slot: EquipmentSlot
    ) {
        if (!enchant.limitations.checkAvailable(CheckType.USE, item, entity, slot).first) return

        byType[eventType]?.filter { byId[it]!!.first == priority }?.forEach listeners@{ id ->

            val sHolders = mutableMapOf<String, String>()
            val fHolders = mutableMapOf<String, Pair<ObjectEntry<*>, String>>()
            fHolders += trigger.flexible

            val chains = byId[id]!!.second
            fun next(tot: Int = 0) {
                if (tot < 0) return // FIXME: 意外情况
                if (tot >= chains.size) return
                val chain = chains[tot]
                sHolders["随机数"] = (Math.random() * 100).roundToInt().toString()
                sHolders += enchant.variables.variables(item.etLevel(enchant), entity, item, false)

                when (chain.type) {
                    ChainType.DELAY -> submit(delay = (chain.content.calcToDouble(sHolders) * 20).roundToLong()) { next(tot + 1) }
                    ChainType.GOTO -> next(chain.content.calcToInt(sHolders) - 1)
                    ChainType.END -> return
                    else -> {
                        val result = chain.trigger(event, eventType, entity, item, sHolders, fHolders)
                        if (chain.type == ChainType.OPERATION) {
                            val parts = result.split(":")
                            if (!parts[0].cbool) next(parts[1].cint - 1)
                            else next(tot + 1)
                        } else {
                            if (!result.cbool && chain.type == ChainType.COOLDOWN && chain.content.split(":").size > 1)
                                next(chain.content.split(":")[1].calcToInt() - 1)
                            if (result.cbool) next(tot + 1)
                        }
                    }
                }
            }
            next()
        }
    }
}