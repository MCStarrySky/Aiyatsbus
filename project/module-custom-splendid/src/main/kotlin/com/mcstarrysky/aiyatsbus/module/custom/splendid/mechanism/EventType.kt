package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism

import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.event.*
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.EventEntry

enum class EventType(val display: String, val entry: EventEntry<*>) {
    ATTACK("攻击", EventAttack),
    KILL("击杀", EventKill),
    RIGHT_CLICK("右击", EventInteract), //包含交互生物
    LEFT_CLICK("左击", EventInteract),
    INTERACT_ENTITY("交互生物", EventInteractEntity),
    PHYSICAL_INTERACT("交互方块", EventInteract),
    DURABILITY_REDUCED("损耗耐久", EventDurabilityReduced),
    DAMAGED("受伤", EventDamaged),
    SNEAK("下蹲", EventSneak),
    FLY("飞行", EventFly),
    CHAT("聊天", EventChat);

    companion object {
        fun getType(identifier: String?): EventType? = values().find { it.display == identifier || it.name == identifier }
    }
}