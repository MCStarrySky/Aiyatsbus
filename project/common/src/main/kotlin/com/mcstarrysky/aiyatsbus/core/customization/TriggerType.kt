package com.mcstarrysky.aiyatsbus.core.customization

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import taboolib.library.reflex.Reflex.Companion.invokeConstructor
import taboolib.module.configuration.Configuration

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.customization.TriggerType
 *
 * @author mical
 * @since 2024/2/19 20:03
 */
enum class TriggerType(val clazz: String) {

    KOTLIN_SCRIPT(""), // 使用 Artifex 驱动的 Kotlin script 触发器
    SPLENDID("com.mcstarrysky.aiyatsbus.module.custom.splendid.SplendidTrigger"); // 从 3.0 继承下来的自定义附魔体系

    companion object {

        /**
         * 加载自定义附魔触发器
         *
         * @param enchant
         * @param config
         * @param load 是否自动初始化
         */
        fun new(enchant: AiyatsbusEnchantment, config: Configuration, load: Boolean = true): CustomTrigger {
            val section = config.getConfigurationSection("trigger")
            val type = TriggerType.valueOf(section?.get("type")?.toString() ?: "SPLENDID")
            return (Class.forName(type.clazz).invokeConstructor(enchant, config, type) as? CustomTrigger ?: error("")).also {
                if (load) it.initialize()
            }
        }
    }
}