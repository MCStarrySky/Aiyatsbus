package com.mcstarrysky.aiyatsbus.core

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.Aiyatsbus
 *
 * @author mical
 * @since 2024/2/17 15:31
 */
object Aiyatsbus {

    private var api: AiyatsbusAPI? = null

    fun api(): AiyatsbusAPI {
        return api ?: error("AiyatsbusAPI has not finished loading, or failed to load.")
    }

    fun register(api: AiyatsbusAPI) {
        Aiyatsbus.api = api
    }
}