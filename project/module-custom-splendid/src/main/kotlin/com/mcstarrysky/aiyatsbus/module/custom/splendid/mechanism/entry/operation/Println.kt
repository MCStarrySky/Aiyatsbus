package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.operation

import com.mcstarrysky.aiyatsbus.core.util.translate
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.platform.util.sendLang

object Println {

    fun println(entity: LivingEntity, text: String) {
        println(text.translate())
        if (entity is Player)
            entity.sendLang(text)
    }
}