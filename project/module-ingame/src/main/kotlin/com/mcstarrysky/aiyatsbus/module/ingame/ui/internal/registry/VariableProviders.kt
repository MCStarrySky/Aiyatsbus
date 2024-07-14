package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.registry

import com.mcstarrysky.aiyatsbus.core.util.SimpleRegistry
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature.util.VariableProvider
import java.util.*

object VariableProviders : SimpleRegistry<String, VariableProvider>(TreeMap(String.CASE_INSENSITIVE_ORDER)) {
    override fun getKey(value: VariableProvider): String = value.name
}