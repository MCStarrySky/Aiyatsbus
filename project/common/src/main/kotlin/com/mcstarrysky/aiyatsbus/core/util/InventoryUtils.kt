package com.mcstarrysky.aiyatsbus.core.util

import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.InventoryUtils
 *
 * @author mical
 * @since 2024/7/14 12:32
 */
val Entity.equippedItems: Map<EquipmentSlot, ItemStack>
    get() = if (this is LivingEntity) EquipmentSlot.values()
        .associateWith { (equipment?.getItem(it) ?: ItemStack(Material.AIR)) } else emptyMap()