package com.mcstarrysky.aiyatsbus.impl

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import taboolib.common.util.unsafeLazy

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
        Aiyatsbus.register(api)
    }
}