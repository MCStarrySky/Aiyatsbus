package com.mcstarrysky.aiyatsbus.impl

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
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
        Language.setProperty("enableSimpleComponent", true) // 凭啥是 val?
        Aiyatsbus.register(api)
    }
}