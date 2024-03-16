package com.mcstarrysky.aiyatsbus.compat.vanilla

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.PacketSendEvent

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.compat.vanilla.PacketSystemChat
 *
 * @author mical
 * @since 2024/2/18 00:40
 */
@RuntimeDependency(value = "!com.google.code.gson:gson:2.10.1", test = "!com.google.gson.JsonElement")
object PacketSystemChat {
    private val gson = GsonComponentSerializer.gson()

    @SubscribeEvent(priority = EventPriority.MONITOR)
    fun e(e: PacketSendEvent) {
        if (e.packet.name == "ClientboundSystemChatPacket") {
            e.packet.source::class.java.declaredFields.forEach { info(it.type.name) }
            runCatching {
                val player = e.player
                if (MinecraftVersion.majorLegacy > 12002) {
                  //  val adventure = Aiyatsbus.api().getMinecraftAPI().iChatBaseComponentToComponent(e.packet.source.getProperty<Any>("content") as? Component ?: return)
                  //  e.packet.source.setProperty("content", Aiyatsbus.api().getMinecraftAPI().componentToIChatBaseComponent(modify(adventure, player)))
                } else {
                    val adventure = e.packet.source.getProperty<Any>("adventure\$content", remap = false) as? Component ?: return
                    e.packet.source.setProperty("adventure\$content", modify(adventure, player), remap = false)
                }
            }.onFailure { it.printStackTrace() }
        }
        //if (e.packet.name == "PacketPlayInChat") {
        //    if (MinecraftVersion.isUniversal) {
        //        info(e.packet.read("message"))
        //    } else info(e.packet.read("a"))
        //}
    }

    private fun modify(component: Component, player: Player): Component {
        var json = gson.serialize(component)
        val stacks = extractHoverEvents(json)

        stacks.forEach { stack ->
            val itemId = stack.get("id").asString + stack.get("tag").asString
            val item = Bukkit.getItemFactory().createItemStack(itemId)
            val display = Aiyatsbus.api().getDisplayManager().display(item, player)

            val target = display.displayName().hoverEvent(display.asHoverEvent())
            json = json.replace(
                stack.get("tag").asString.flat(),
                extractHoverEvents(gson.serialize(target)).first().get("tag").asString.flat()
            )
        }

        return gson.deserialize(json)
    }

    private fun extractHoverEvents(jsonString: String): List<JsonObject> {
        val jsonObject = JsonParser.parseString(jsonString).asJsonObject
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