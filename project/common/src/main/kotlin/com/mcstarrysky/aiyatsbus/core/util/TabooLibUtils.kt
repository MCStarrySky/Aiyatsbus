package com.mcstarrysky.aiyatsbus.core.util

import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.LocaleI18n
import taboolib.module.nms.getLocaleFile

/**
 * 兼容 1.21
 *
 * @author mical
 * @since 2024/8/17 00:55
 */

fun Entity.getI18nName(player: Player? = null): String {
    val localeFile = player?.getLocaleFile() ?: LocaleI18n.getDefaultLocaleFile() ?: return "NO_LOCALE"
    val localeKey = type.translationKey()
    return localeFile[localeKey] ?: localeKey
}

fun ItemStack.getI18nName(player: Player? = null): String {
    val localeFile = player?.getLocaleFile() ?: LocaleI18n.getDefaultLocaleFile() ?: return "NO_LOCALE"
    val localeKey = translationKey()
    return localeFile[localeKey] ?: localeKey
}