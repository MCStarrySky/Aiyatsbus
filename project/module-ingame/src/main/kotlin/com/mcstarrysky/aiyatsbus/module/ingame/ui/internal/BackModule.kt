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
package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import com.mcstarrysky.aiyatsbus.module.ingame.ui.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.util.replaceWithOrder
import java.util.*

val recorders = mutableMapOf<UUID, MutableList<Pair<UIType, Map<String, Any?>>>>()

fun Player.record(type: UIType, vararg pairs: Pair<String, Any?>) {
    val recorder = recorders.getOrPut(uniqueId) { mutableListOf() }
    val last = recorder.size - 1
    val holders = pairs.toMap()
    if (type == UIType.ENCHANT_INFO) {
        val current = holders["enchant"] as AiyatsbusEnchantment
        val lastEt = recorder[last].second["enchant"] as? AiyatsbusEnchantment
        if (current.basicData.id != lastEt?.basicData?.id) {
            recorder += type to holders
        }
    } else if (recorder.lastOrNull()?.first == type) recorder[last] = type to holders
    else recorder += type to holders

}

fun Player.last(): String {
    val recorder = recorders[uniqueId]!!
    val size = recorder.size
    if (size == 0) return "N/A"
    if (size == 1) return forceLast(recorder[0].first, this)
    val last = recorder[size - 2]
    val type = last.first
    val params = last.second
    return type.display(this)?.replaceWithOrder(when (type) {
        UIType.ENCHANT_INFO -> (params["enchant"] as AiyatsbusEnchantment).displayName(params["level"] as Int)
        else -> ""
    }) ?: UIType.UNKNOWN.display(this) ?: "N/A"
}

fun forceLast(type: UIType, sender: CommandSender): String {
    return when (type) {
        UIType.ANVIL -> UIType.MAIN_MENU
        UIType.ENCHANT_INFO -> UIType.ENCHANT_SEARCH
        UIType.ENCHANT_SEARCH -> UIType.MAIN_MENU
        UIType.FILTER_GROUP -> UIType.ENCHANT_SEARCH
        UIType.FILTER_RARITY -> UIType.ENCHANT_SEARCH
        UIType.FILTER_TARGET -> UIType.ENCHANT_SEARCH
        UIType.ITEM_CHECK, UIType.UNKNOWN -> null
        UIType.MAIN_MENU -> UIType.MAIN_MENU
        UIType.FAVORITE -> UIType.MAIN_MENU
    }?.display(sender) ?: UIType.UNKNOWN.display(sender) ?: "N/A"
}

fun Player.forceBack(type: UIType) {
    when (type) {
        UIType.ANVIL -> MainMenuUI.open(this)
        UIType.ENCHANT_INFO -> EnchantSearchUI.open(this)
        UIType.ENCHANT_SEARCH -> MainMenuUI.open(this)
        UIType.FILTER_GROUP -> EnchantSearchUI.open(this)
        UIType.FILTER_RARITY -> EnchantSearchUI.open(this)
        UIType.FILTER_TARGET -> EnchantSearchUI.open(this)
        UIType.ITEM_CHECK -> MainMenuUI.open(this)
        UIType.MAIN_MENU -> performCommand(AiyatsbusSettings.mainMenuBack).also { recorders.remove(uniqueId) }
        UIType.FAVORITE -> MainMenuUI.open(this)
        UIType.UNKNOWN -> {
        }
    }
}

fun Player.back() {
    val recorder = recorders[uniqueId]!!
    val size = recorder.size
    if (size == 1) {
        forceBack(recorder[0].first)
        return
    }
    val last = recorder[size - 2]
    recorder.removeAt(size - 1)
    val params = last.second
    when (last.first) {
        UIType.ANVIL -> AnvilUI.open(this, params["a"] as? ItemStack, params["b"] as? ItemStack)
        UIType.ENCHANT_INFO -> EnchantInfoUI.open(this, params)
        UIType.ENCHANT_SEARCH -> EnchantSearchUI.open(this)
        UIType.FILTER_GROUP -> FilterGroupUI.open(this)
        UIType.FILTER_RARITY -> FilterRarityUI.open(this)
        UIType.FILTER_TARGET -> FilterTargetUI.open(this)
        UIType.ITEM_CHECK -> ItemCheckUI.open(this, params["item"] as? ItemStack, params["mode"] as ItemCheckUI.CheckMode)
        UIType.MAIN_MENU -> MainMenuUI.open(this)
        UIType.FAVORITE -> {} // TODO
        UIType.UNKNOWN -> {
        }
    }
}

object Recorder {

    @SubscribeEvent
    fun close(event: InventoryCloseEvent) {
        if (event.reason != InventoryCloseEvent.Reason.OPEN_NEW)
            recorders.remove(event.player.uniqueId)
    }
}