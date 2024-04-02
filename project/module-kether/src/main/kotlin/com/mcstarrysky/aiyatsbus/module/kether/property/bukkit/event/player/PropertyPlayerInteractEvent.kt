package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player

import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.event.player.PropertyPlayerInteractEvent
 *
 * @author mical
 * @since 2024/3/11 22:32
 */
@AiyatsbusProperty(
    id = "player-interact-event",
    bind = PlayerInteractEvent::class
)
class PropertyPlayerInteractEvent : AiyatsbusGenericProperty<PlayerInteractEvent>("player-interact-event") {

    override fun readProperty(instance: PlayerInteractEvent, key: String): OpenResult {
        val property: Any? = when (key) {
            "action" -> instance.action.name
            "isBlockInHand", "is-block-in-hand", "in-hand" -> instance.isBlockInHand
            "isLeftClick", "is-left-click", "is-left", "left-click" -> instance.action == Action.LEFT_CLICK_AIR || instance.action == Action.LEFT_CLICK_BLOCK
            "isRightClick", "is-right-click", "is-right", "right-click" -> instance.action == Action.RIGHT_CLICK_AIR || instance.action == Action.RIGHT_CLICK_BLOCK
            "isClickAir", "is-click-air", "click-air" -> instance.action == Action.LEFT_CLICK_AIR || instance.action == Action.RIGHT_CLICK_AIR
            "isClickBlock", "is-click-block", "click-block" -> instance.action == Action.LEFT_CLICK_BLOCK || instance.action == Action.RIGHT_CLICK_BLOCK
            "isPhysical", "is-physical" -> instance.action == Action.PHYSICAL
            "hand" -> instance.hand?.name ?: "NULL"
            "item" -> instance.item
            "block", "clickedBlock" -> instance.clickedBlock
            "blockFace", "block-face" -> instance.blockFace
            "hasBlock", "has-block" -> instance.hasBlock()
            "hasItem", "has-item" -> instance.hasItem()
            "isBlockPlace", "is-block-place", "is-place" -> instance.isBlockInHand
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: PlayerInteractEvent, key: String, value: Any?): OpenResult {
        return OpenResult.failed()
    }
}