package com.mcstarrysky.aiyatsbus.module.kether

import kotlin.reflect.KClass

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
 *
 * @author Lanscarlos
 * @since 2023-03-19 22:12
 */
annotation class AiyatsbusProperty(
    val id: String,
    val bind: KClass<*>
)