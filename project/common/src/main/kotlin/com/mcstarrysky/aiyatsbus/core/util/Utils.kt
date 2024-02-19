package com.mcstarrysky.aiyatsbus.core.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta
import sun.misc.Unsafe
import taboolib.platform.util.isAir
import taboolib.platform.util.modifyMeta
import java.lang.reflect.Field

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.registration.modern.Utils
 *
 * @author mical
 * @since 2024/2/17 17:07
 */
val LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
    .character('\u00a7')
    .useUnusualXRepeatedCharacterHexFormat()
    .hexColors()
    .build()

fun String.toAdventureComponent(): Component {
    return LEGACY_COMPONENT_SERIALIZER.deserialize(this)
}

private val unsafe = Unsafe::class.java.getDeclaredField("theUnsafe")
    .apply { isAccessible = true }
    .get(null) as Unsafe

fun Field.setStaticFinal(value: Any) {
    val offset = unsafe.staticFieldOffset(this)
    unsafe.putObject(unsafe.staticFieldBase(this), offset, value)
}

val ItemStack?.isNull get() = this?.isAir ?: true

val ItemStack.isEnchantedBook get() = itemMeta is EnchantmentStorageMeta

var ItemStack.name
    get() = itemMeta?.displayName
    set(value) {
        modifyMeta<ItemMeta> { setDisplayName(value) }
    }

var ItemStack.damage
    get() = (itemMeta as? Damageable)?.damage ?: 0
    set(value) {
        modifyMeta<Damageable> { damage = value }
    }