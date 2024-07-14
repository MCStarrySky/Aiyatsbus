package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.registry

import com.mcstarrysky.aiyatsbus.core.util.SimpleRegistry
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature.util.MenuFunction
import java.util.*

object MenuFunctions : SimpleRegistry<String, MenuFunction>(TreeMap(String.CASE_INSENSITIVE_ORDER)) {
    override fun getKey(value: MenuFunction): String = value.name
}