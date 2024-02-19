@file:Suppress("deprecation")

package com.mcstarrysky.aiyatsbus.core.util

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.ItemTag
import net.md_5.bungee.api.chat.hover.content.Item
import net.md_5.bungee.chat.ComponentSerializer
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.ComponentUtils
 *
 * @author mical
 * @since 2024/2/18 00:39
 */
object ComponentUtils {

    /**
     * 处理聊天展示
     */
    fun applyItemDisplay(component: BaseComponent, player: Player): BaseComponent {
        val nms = Aiyatsbus.api().getMinecraftAPI()
        val queue = ConcurrentLinkedQueue<BaseComponent>()
        queue.add(component)
        var processing: BaseComponent
        while (queue.isNotEmpty()) {
            processing = queue.poll()
            processing.extra?.let { queue.addAll(it) }

            val hover = processing.hoverEvent ?: continue
            if (hover.action == HoverEvent.Action.SHOW_ITEM) {
                val content = hover.contents[0] as Item
                val id = content.id
                val cnt = content.count
                // println("未修改物品的nbt: " + nms.itemToJson(content))
                // println("未修改物品的bukkit物品堆: " + nms.jsonToItem(nms.itemToJson(content)))
                val item = nms.jsonToItem(nms.itemToJson(content)) // 这是未修改过的物品
                val newItem = Aiyatsbus.api().getDisplayManager().display(item, player) // 生成展示过的物品
                // println("新物品nbt: " + nms.bkItemToJson(newItem))
                val newNBT = nms.bkItemToJson(newItem) // 新物品 NBT

                hover.contents[0] = Item(id, cnt, ItemTag.ofNbt(newNBT)) // 设置回去
                processing.hoverEvent = hover
            }
        }
        return component
    }

    /**
     * 将 BaseComponent 转为原始信息
     */
    fun parseRaw(component: BaseComponent): String {
        return ComponentSerializer.toString(component)
    }
}