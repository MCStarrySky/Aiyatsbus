package com.mcstarrysky.aiyatsbus.module.ui.internal

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import com.mcstarrysky.aiyatsbus.module.ui.*
import com.mcstarrysky.aiyatsbus.module.ui.internal.feature.util.MenuFunctionBuilder
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import com.mcstarrysky.aiyatsbus.module.ui.internal.function.variable
import taboolib.common.platform.event.SubscribeEvent
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
    if (size == 1) return forceLast(recorder[0].first)
    val last = recorder[size - 2]
    val type = last.first
    val params = last.second
    return "${type.display} " + when (type) {
        UIType.ENCHANT_INFO -> (params["enchant"] as AiyatsbusEnchantment).displayName(params["level"] as Int)
        else -> ""
    }
}

fun forceLast(type: UIType): String {
    return when (type) {
        UIType.ANVIL -> UIType.MAIN_MENU
        UIType.ENCHANT_INFO -> UIType.ENCHANT_SEARCH
        UIType.ENCHANT_SEARCH -> UIType.MAIN_MENU
        UIType.FILTER_GROUP -> UIType.ENCHANT_SEARCH
        UIType.FILTER_RARITY -> UIType.ENCHANT_SEARCH
        UIType.FILTER_TARGET -> UIType.ENCHANT_SEARCH
        UIType.ITEM_CHECK -> null
        UIType.MAIN_MENU -> UIType.MAIN_MENU
        UIType.FAVORITE -> UIType.MAIN_MENU
    }?.display ?: "N/A"
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
        UIType.FAVORITE -> {}
    }
}

@MenuComponent
val back = MenuFunctionBuilder {
    onBuild { (_, _, _, _, icon, args) -> icon.variable("last", listOf((args["player"] as Player).last())) }
    onClick { (_, _, _, event, _) -> event.clicker.back() }
}

object Recorder {

    @SubscribeEvent
    fun close(event: InventoryCloseEvent) {
        if (event.reason != InventoryCloseEvent.Reason.OPEN_NEW)
            recorders.remove(event.player.uniqueId)
    }
}