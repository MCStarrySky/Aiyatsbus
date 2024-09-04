package com.mcstarrysky.aiyatsbus.core.util

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import com.mcstarrysky.aiyatsbus.core.compat.NPCChecker
import dev.lone.itemsadder.api.CustomBlock
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval
import taboolib.module.nms.LocaleI18n
import taboolib.module.nms.getLocaleFile
import kotlin.math.PI

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.EntityUtils
 *
 * @author mical
 * @since 2024/7/14 15:30
 */
@ScheduledForRemoval(inVersion = "1.0")
@Deprecated("only made for 1.21 support")
fun Entity.getI18nName(player: Player? = null): String {
    val localeFile = player?.getLocaleFile() ?: LocaleI18n.getDefaultLocaleFile() ?: return "NO_LOCALE"
    val localeKey = type.translationKey()
    return localeFile[localeKey] ?: localeKey
}

fun Entity?.checkIfIsNPC(): Boolean {
    return NPCChecker.checkIfIsNPC(this ?: return false)
}

fun LivingEntity.isBehind(entity: LivingEntity): Boolean {
    if (world.name != entity.world.name) {
        return false
    }
    val directionSelf = entity.location.clone().subtract(location).toVector()
    directionSelf.setY(0)
    val direction = entity.location.direction
    direction.setY(0)
    return directionSelf.angle(direction) < PI / 3
}

val Entity.equippedItems: Map<EquipmentSlot, ItemStack>
    get() = if (this is LivingEntity) EquipmentSlot.values()
        .associateWith { (equipment?.getItem(it) ?: ItemStack(Material.AIR)) } else emptyMap()

/**
 * 对 LivingEntity 造成真实伤害, 插件和原版无法减伤
 */
fun LivingEntity.realDamage(amount: Double, who: Entity? = null) {
    health = maxOf(0.1, health - amount + 0.5)
    damage(0.5, who)
    if (isDead) health = 0.0
}

/**
 * 令玩家放置方块
 */
fun Player.placeBlock(placedBlock: Block, itemInHand: ItemStack = this.itemInHand): Boolean {
    val blockAgainst = placedBlock.getRelative(0, 1, 0)
    val event = BlockPlaceEvent(placedBlock, placedBlock.state, blockAgainst, itemInHand, this, true)
    return event.callEvent()
}

fun Player.doBreakBlock(block: Block) {
    try {
        block.mark("block-ignored")
        Aiyatsbus.api().getMinecraftAPI().breakBlock(this, block)
    } catch (ex: Throwable) {
        ex.printStackTrace()
    } finally {
        if (block.type != Material.AIR) {
            if (AiyatsbusSettings.supportItemsAdder && itemsAdderEnabled) {
                if (CustomBlock.byAlreadyPlaced(block) != null) {
                    CustomBlock.getLoot(block, inventory.itemInMainHand, true).forEach {
                        world.dropItem(block.location, it)
                    }
                    CustomBlock.remove(block.location)
                } else {
                    block.breakNaturally(inventory.itemInMainHand)
                }
            } else {
                block.breakNaturally(inventory.itemInMainHand)
            }
        }
        block.unmark("block-ignored")
    }
}