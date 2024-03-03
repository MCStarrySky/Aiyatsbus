package com.mcstarrysky.aiyatsbus.impl

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.StandardPriorities
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.common.platform.function.info
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.common.util.unsafeLazy
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.lang.Language

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.registration.legacy.DefaultAiyatsbusBooster
 *
 * @author mical
 * @since 2024/2/17 16:19
 */
object DefaultAiyatsbusBooster {

    val api by unsafeLazy { DefaultAiyatsbusAPI() }

    fun startup() {
        Language.enableSimpleComponent = true
        Aiyatsbus.register(api)
    }
}