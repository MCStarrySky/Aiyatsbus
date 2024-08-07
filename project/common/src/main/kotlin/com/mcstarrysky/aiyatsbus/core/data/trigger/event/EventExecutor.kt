package com.mcstarrysky.aiyatsbus.core.data.trigger.event

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import taboolib.common.platform.function.warning
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.kether.LocalizedException

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.trigger.EventExecutor
 *
 * @author mical
 * @since 2024/3/9 18:35
 */
data class EventExecutor @JvmOverloads constructor(
    private val root: ConfigurationSection,
    private val enchant: AiyatsbusEnchantment,
    val listen: String = root.getString("listen")!!,
    val handle: String = root.getString("handle") ?: "",
    val priority: Int = root.getInt("priority", 0)
) {

    init {
        if (AiyatsbusSettings.enableKetherPreheat) {
            try {
                Aiyatsbus.api().getKetherHandler().preheat(handle)
            } catch (ex: LocalizedException) {
                warning("Unable to preheat the event executor ${root.name} of enchantment ${enchant.id}: $ex")
            }
        }
    }
}