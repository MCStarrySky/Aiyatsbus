package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.registry

import com.mcstarrysky.aiyatsbus.core.util.container.SimpleRegistry
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature.util.MenuOpener
import java.util.*

object MenuOpeners : SimpleRegistry<String, MenuOpener>(TreeMap(String.CASE_INSENSITIVE_ORDER)) {
    override fun getKey(value: MenuOpener): String = value.name
}