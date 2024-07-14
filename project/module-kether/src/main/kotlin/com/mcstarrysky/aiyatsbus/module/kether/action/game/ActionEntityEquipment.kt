package com.mcstarrysky.aiyatsbus.module.kether.action.game

import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.game.ActionEntityEquipment
 *
 * @author mical
 * @since 2024/7/14 13:37
 */
object ActionEntityEquipment {

    @KetherParser(["equip-set-item"], shared = true)
    fun equipSetItem() = combinationParser {
        it.group(
            type<ItemStack?>(),
            command("in", then = any()),
            command("to", "on", then = type<LivingEntity>())
        ).apply(it) { item, slot, entity ->
            now { entity.equipment?.setItem(slot as? EquipmentSlot ?: EquipmentSlot.valueOf(slot.toString()), item) }
        }
    }
}