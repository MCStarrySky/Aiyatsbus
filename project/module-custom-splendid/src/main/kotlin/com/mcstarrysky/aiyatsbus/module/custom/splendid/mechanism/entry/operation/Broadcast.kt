package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.operation

import com.mcstarrysky.aiyatsbus.core.util.translate
import taboolib.platform.util.onlinePlayers
import taboolib.platform.util.sendLang

object Broadcast {
    fun broadcast(text: String) {
        println(text.translate())
        onlinePlayers.forEach { it.sendLang(text) }
    }
}