package com.mcstarrysky.aiyatsbus.module.compat.chat.display

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.toDisplayMode
import com.mcstarrysky.aiyatsbus.core.util.isValidJson
import com.mcstarrysky.aiyatsbus.module.compat.chat.DisplayReplacer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.entity.Player
import taboolib.module.nms.*

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.compat.chat.display.DisplayReplacerDataComponents
 *
 * @author xiaozhangup
 * @since 2024/8/18 16:43
 */
object DisplayReplacerDataComponents : DisplayReplacer {

    private val gson = GsonComponentSerializer.gson()

    override fun apply(component: Any, player: Player): Any {
        val json = when (component) {
            is Component -> gson.serialize(component) // Adventure Component
            is String -> component // Json
            else -> Aiyatsbus.api().getMinecraftAPI().componentToJson(component) // 大胆假设是 IChatBaseComponent
        }

        // 尝试修复 Source: '' 的警告
        if (!json.isValidJson()) return component

        val jsonObject = JsonParser.parseString(json).asJsonObject
        applyHoverEvents(jsonObject, player)
        return when (component) {
            is Component -> gson.deserialize(jsonObject.toString())
            is String -> jsonObject.toString()
            else -> Aiyatsbus.api().getMinecraftAPI().componentFromJson(jsonObject.toString())
        }
    }

    private fun applyHoverEvents(obj: Any, player: Player) {
        when (obj) {
            is JsonObject -> {
                if (obj.has("hoverEvent")) {
                    val hoverEvent = obj.getAsJsonObject("hoverEvent")
                    if (hoverEvent.has("action") && hoverEvent.get("action").asString == "show_item") {
                        val contents = hoverEvent.getAsJsonObject("contents")
                        applyDisplay(contents, hoverEvent, player)
                    }
                }

                obj.keySet().forEach { key ->
                    applyHoverEvents(obj.get(key), player)
                }
            }

            is JsonArray -> {
                for (i in 0 until obj.size()) {
                    applyHoverEvents(obj.get(i), player)
                }
            }
        }
    }

    private fun applyDisplay(contents: JsonObject, hoverEvent: JsonObject, player: Player) {
        val json = contents.toString()
        val item = NMSItemTag.instance.fromMinecraftJson(json) ?: return

        val newHoverEvent = GsonComponentSerializer.gson()
            .serialize(Component.empty().hoverEvent(item.toDisplayMode(player).asHoverEvent()))
        val jsonStructure = JsonParser.parseString(newHoverEvent).asJsonObject
        hoverEvent.add("contents", jsonStructure.getAsJsonObject("hoverEvent").getAsJsonObject("contents"))
    }
}