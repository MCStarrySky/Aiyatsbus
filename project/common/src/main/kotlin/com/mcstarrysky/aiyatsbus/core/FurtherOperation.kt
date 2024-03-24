package com.mcstarrysky.aiyatsbus.core

import com.mcstarrysky.aiyatsbus.core.util.serialized
import dev.lone.itemsadder.api.CustomBlock
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.FurtherOperation
 *
 * @author mical
 * @since 2024/3/21 20:28
 */
object FurtherOperation {

    private val operation = ConcurrentHashMap<UUID, MutableList<String>>()
    private val itemsAdderEnabled = runCatching { Class.forName("dev.lone.itemsadder.api.ItemsAdder") }.isSuccess

    fun addOperation(player: Player, name: String, data: String? = null) {
        val op = operation.computeIfAbsent(player.uniqueId) { mutableListOf() }
        op.add("name" + if (data != null) " $data" else "")
    }

    fun removeOperation(player: Player, name: String, data: String? = null) {
        val op = operation[player.uniqueId] ?: return
        op.remove("name" + if (data != null) " $data" else "")
    }

    fun hasOperation(player: Player, name: String, data: String? = null): Boolean {
        val op = operation[player.uniqueId] ?: return false
        return op.contains("name" + if (data != null) " $data" else "")
    }

    fun furtherBreak(player: Player, block: Block) {
        try {
            addOperation(player, "BlockBreakEvent", block.location.serialized)
            Aiyatsbus.api().getMinecraftAPI().breakBlock(player, block)
        } catch (ex: Throwable) {
            ex.printStackTrace()
        } finally {
            if (block.type != Material.AIR) {
                if (AiyatsbusSettings.supportItemsAdder && itemsAdderEnabled) {
                    if (CustomBlock.byAlreadyPlaced(block) != null) {
                        CustomBlock.getLoot(block, player.inventory.itemInMainHand, true).forEach {
                            player.world.dropItem(block.location, it)
                        }
                        CustomBlock.remove(block.location)
                    } else {
                        block.breakNaturally(player.inventory.itemInMainHand)
                    }
                } else {
                    block.breakNaturally(player.inventory.itemInMainHand)
                }
            }
            removeOperation(player, "BlockBreakEvent", block.location.serialized)
        }
    }
}