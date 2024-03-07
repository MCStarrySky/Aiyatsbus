package com.mcstarrysky.aiyatsbus.core.util

import com.google.gson.Gson
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.FluidCollisionMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.potion.PotionEffectType
import sun.misc.Unsafe
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.function.console
import taboolib.module.lang.asLangText
import taboolib.module.nms.getI18nName
import taboolib.platform.util.*
import java.lang.reflect.Field
import java.util.*

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

val GSON = Gson()

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

fun String.translate(who: ProxyCommandSender = console()) = who.asLangText(this)

fun Player.blockLookingAt(
    range: Double = 50.0,
    fluidCollisionMode: FluidCollisionMode = FluidCollisionMode.NEVER
) = rayTraceBlocks(range, fluidCollisionMode)?.hitBlock

fun Player.takeItem(amount: Int = 1, matcher: (itemStack: ItemStack) -> Boolean): Boolean {
    if (inventory.countItem(matcher) >= amount) {
        inventory.takeItem(amount, matcher)
        return true
    }
    return false
}

fun LivingEntity.mainHand() = equipment?.itemInMainHand

fun LivingEntity.offHand() = equipment?.itemInOffHand

fun LivingEntity.effect(type: PotionEffectType, duration: Int, level: Int = 1) {
    addPotionEffect(type.createEffect(duration * 20, level - 1))
}

fun LivingEntity.realDamage(amount: Double, who: Entity? = null) {
    health = maxOf(0.1, health - amount + 0.5)
    damage(0.5, who)
}

val Entity.displayName get() = (this as? Player)?.name ?: customName ?: getI18nName()

val LivingEntity.blockBelow
    get():Block? {
        val loc = location
        repeat(loc.blockY + 63) {
            val current = loc.clone()
            current.y -= it.toDouble() + 1
            if (current.block.type != Material.AIR) {
                return current.block
            }
        }
        return null
    }

fun ItemStack.serializeToBase64(): String {
    return Base64.getEncoder().encodeToString(serializeToByteArray())
}

fun String.deserializeItemStackFromBase64(): ItemStack {
    return Base64.getDecoder().decode(this).deserializeToItemStack()
}

val itemsAdderEnabled = runCatching { Class.forName("dev.lone.itemsadder.api.ItemsAdder") }.isSuccess