@file:Suppress("deprecation")

package com.mcstarrysky.aiyatsbus.module.compat.chat

import com.google.gson.JsonObject
import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.toDisplayMode
import com.mcstarrysky.aiyatsbus.core.util.JSON_PARSER
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.entity.Player
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.PacketSendEvent

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.compat.vanilla.PacketSystemChat
 *
 * @author mical
 * @since 2024/2/18 00:40
 */
object PacketSystemChat {

    private val gson = GsonComponentSerializer.gson()

    @SubscribeEvent(priority = EventPriority.MONITOR)
    fun e(e: PacketSendEvent) {
        val player = e.player
        // 1.19 +
        if (e.packet.name == "ClientboundSystemChatPacket") {
            // 1.20.2 + 取消了 adventure$content, 所以要直接修改 IChatBaseComponent
            // FIXME: 小张看这里有一个 a/adventure 指向 Adventure Component, 但我怎么都测试不出来这个字段
            if (MinecraftVersion.majorLegacy > 12002) {
                val adventure = Aiyatsbus.api().getMinecraftAPI().iChatBaseComponentToComponent(e.packet.source.getProperty<Any>("content") ?: return)
                e.packet.source.setProperty("content", Aiyatsbus.api().getMinecraftAPI().componentToIChatBaseComponent(
                    modify(adventure, player)
                ))
            } else {
                // 1.19 - 1.20.2 是有一个 adventure$content 存储 Adventure Component, 直接修改这个字段即可
                val adventure = e.packet.source.getProperty<Any>("adventure\$content", remap = false) as? Component ?: return
                e.packet.source.setProperty("adventure\$content", modify(adventure, player), remap = false)
            }
        }
        // 1.16 - 1.18 的数据包与高版本不同, 要修改 PacketPlayOutChat 的 message, message 是 IChatBaseComponent
        if (e.packet.name == "PacketPlayOutChat") {
            val field = if (MinecraftVersion.isUniversal) "message" else "a"
            val adventure = Aiyatsbus.api().getMinecraftAPI().iChatBaseComponentToComponent(e.packet.source.getProperty<Any>(field) ?: return)
            e.packet.source.setProperty(field, Aiyatsbus.api().getMinecraftAPI().componentToIChatBaseComponent(
                modify(adventure, player)
            ))
        }
    }

    fun modify(component: Component, player: Player): Component {
        try {
            var json = gson.serialize(component)

            try {
                // 弱者做法: 二次解析, 防止 GsonComponentSerializer 把单引号解析成 \u0027
                // TODO: warning: Source: 'xxxxx'
                json = Configuration.loadFromString(json, Type.FAST_JSON).saveToString()
            } catch (_: Throwable) {
                return component
            }

            val stacks = extractHoverEvents(json)

            for (stack in stacks) {
                val id = stack.get("id").asString
                val tag = stack.get("tag")?.asString ?: continue

                val item = Aiyatsbus.api().getMinecraftAPI().createItemStack(id, tag)
                val display = item.toDisplayMode(player)

                val target = display.displayName().hoverEvent(display.asHoverEvent())
                json = json.replace(
                    stack.get("tag").asString.flat(),
                    extractHoverEvents(gson.serialize(target)).first().get("tag").asString.flat()
                )
            }

            return gson.deserialize(json)
        } catch (_: Throwable) {
            return component
        }
    }

    private fun extractHoverEvents(jsonString: String): List<JsonObject> {
        val jsonObject = JSON_PARSER.parse(jsonString).asJsonObject
        val hoverEvents = mutableListOf<JsonObject>()

        findHoverEvents(jsonObject, hoverEvents)

        return hoverEvents
    }

    private fun findHoverEvents(jsonObject: JsonObject, hoverEvents: MutableList<JsonObject>) {
        for (entry in jsonObject.entrySet()) {
            val value = entry.value
            if (value.isJsonObject) {
                if (entry.key == "hoverEvent" && value.asJsonObject.has("action") && value.asJsonObject.get("action").asString == "show_item") {
                    hoverEvents.add(value.asJsonObject.get("contents").asJsonObject)
                } else {
                    findHoverEvents(value.asJsonObject, hoverEvents)
                }
            } else if (value.isJsonArray) {
                for (element in value.asJsonArray) {
                    if (element.isJsonObject) {
                        findHoverEvents(element.asJsonObject, hoverEvents)
                    }
                }
            }
        }
    }

    private fun String.flat(): String {
        return this.replace("\"", "\\\"")
    }
}