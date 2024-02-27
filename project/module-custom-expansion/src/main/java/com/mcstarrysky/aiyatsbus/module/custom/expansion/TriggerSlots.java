package com.mcstarrysky.aiyatsbus.module.custom.expansion;

import org.bukkit.inventory.EquipmentSlot;

import java.util.Set;

/**
 * Expansion 模块要求全 Java, 所以单独又写了一份
 *
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.custom.expansion.TriggerSlots
 *
 * @author mical
 * @since 2024/2/27 21:57
 */
public enum TriggerSlots {

    ARMORS(Set.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)),
    HANDS(Set.of(EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)),
    MAIN_HAND(Set.of(EquipmentSlot.HAND)),
    OFF_HAND(Set.of(EquipmentSlot.OFF_HAND)),
    HELMET(Set.of(EquipmentSlot.HEAD)),
    CHESTPLATE(Set.of(EquipmentSlot.CHEST)),
    LEGGINGS(Set.of(EquipmentSlot.LEGS)),
    BOOTS(Set.of(EquipmentSlot.FEET)),
    ALL(Set.of(EquipmentSlot.values()));

    private final Set<EquipmentSlot> slots;

    TriggerSlots(final Set<EquipmentSlot> slots) {
        this.slots = slots;
    }

    public Set<EquipmentSlot> getSlots() {
        return slots;
    }
}
