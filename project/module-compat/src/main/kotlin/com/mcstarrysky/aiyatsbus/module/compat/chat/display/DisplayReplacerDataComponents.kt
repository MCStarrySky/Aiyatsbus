package com.mcstarrysky.aiyatsbus.module.compat.chat.display

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mcstarrysky.aiyatsbus.core.toDisplayMode
import com.mcstarrysky.aiyatsbus.core.util.isValidJson
import com.mcstarrysky.aiyatsbus.module.compat.chat.DisplayReplacer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.entity.Player
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.module.nms.ItemTag

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.compat.chat.display.DisplayReplacerDataComponents
 *
 * @author xiaozhangup
 * @since 2024/8/18 16:43
 */
object DisplayReplacerDataComponents : DisplayReplacer {
    private val gson = GsonComponentSerializer.gson()

    override fun apply(component: Component, player: Player): Component {
        try {
            var json = gson.serialize(component)

            if (!json.isValidJson()) return component

            try {
                // 弱者做法: 二次解析, 防止 GsonComponentSerializer 把单引号解析成 \u0027
                json = Configuration.loadFromString(json, Type.FAST_JSON).saveToString()
            } catch (_: Throwable) {
                return component
            }
            val stacks = extractItemContentsFromJson(
                JsonParser.parseString(json).asJsonObject
            )

            for (stack in stacks) {
                val from = stack.toString()
                val to = ItemTag.toJson(
                    ItemTag.toItem(from).toDisplayMode(player)
                )

                json = json.replace(from, to)
            }

            return gson.deserialize(json)
        } catch (_: Throwable) {
            return component
        }
    }

    private fun extractItemContentsFromJson(json: JsonObject): List<JsonObject> {
        val results = mutableListOf<JsonObject>()

        fun extractRecursive(obj: Any) {
            when (obj) {
                is JsonObject -> {
                    if (obj.has("hoverEvent")) {
                        val hoverEvent = obj.getAsJsonObject("hoverEvent")
                        if (hoverEvent.has("action") && hoverEvent.get("action").asString == "show_item") {
                            val contents = hoverEvent.getAsJsonObject("contents")
                            results.add(contents)
                        }
                    }

                    obj.keySet().forEach { key ->
                        extractRecursive(obj.get(key))
                    }
                }
                is JsonArray -> {
                    for (i in 0 until obj.size()) {
                        extractRecursive(obj.get(i))
                    }
                }
            }
        }

        // 递归扫描防止某些神秘的嵌套数层的消息
        extractRecursive(json)

        return results
    }
}