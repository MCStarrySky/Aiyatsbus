package com.mcstarrysky.aiyatsbus.impl.registration.modern

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentBase
import com.mcstarrysky.aiyatsbus.core.registration.modern.ModernEnchantmentRegisterer
import com.mcstarrysky.aiyatsbus.core.util.setStaticFinal
import net.minecraft.core.Holder
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.enchantment.Enchantments
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_20_R3.CraftRegistry
import org.bukkit.craftbukkit.v1_20_R3.CraftServer
import org.bukkit.craftbukkit.v1_20_R3.enchantments.CraftEnchantment
import org.bukkit.craftbukkit.v1_20_R3.util.CraftNamespacedKey
import org.bukkit.enchantments.Enchantment
import java.util.*
import kotlin.collections.HashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.registration.modern.DefaultModernEnchantmentRegisterer
 *
 * @author mical
 * @since 2024/2/17 17:28
 */
class DefaultModernEnchantmentRegisterer : ModernEnchantmentRegisterer {

    private val frozenField = MappedRegistry::class.java
        .declaredFields
        .filter { it.type.isPrimitive }[0]
        .apply { isAccessible = true }

    private val unregisteredIntrusiveHoldersField = MappedRegistry::class.java
        .declaredFields
        .last { it.type == Map::class.java }
        .apply { isAccessible = true }

    @Suppress("UNCHECKED_CAST")
    private val registries = CraftServer::class.java
        .getDeclaredField("registries")
        .apply { isAccessible = true }
        .get(Bukkit.getServer())
            as HashMap<Class<*>, org.bukkit.Registry<*>>

    private val vanillaEnchantments = Enchantments::class.java
        .declaredFields
        .filter { it.type == net.minecraft.world.item.enchantment.Enchantment::class.java }
        .map { it.get(null) as net.minecraft.world.item.enchantment.Enchantment }
        .mapNotNull { BuiltInRegistries.ENCHANTMENT.getKey(it) }
        .map { CraftNamespacedKey.fromMinecraft(it) }
        .toSet()

    override fun replaceRegistry() {
        val server = Bukkit.getServer() as CraftServer

        @Suppress("UNCHECKED_CAST")
        val registry = CraftRegistry(
            Enchantment::class.java as Class<in Enchantment?>,
            server.handle.server.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
        ) { key, registry ->
            val isVanilla = vanillaEnchantments.contains(key)
            val aiyatsbus = Aiyatsbus.api().getEnchantmentManager().getByID(key.key)

            if (isVanilla) {
                CraftEnchantment(key, registry)
            } else if (aiyatsbus != null) {
                aiyatsbus as Enchantment
            } else null
        }

        // Register to server
        registries[Enchantment::class.java] = registry

        // Register to API
        org.bukkit.Registry::class.java
            .getDeclaredField("ENCHANTMENT")
            .setStaticFinal(registry)

        // Unfreeze NMS registry
        frozenField.set(BuiltInRegistries.ENCHANTMENT, false)
        unregisteredIntrusiveHoldersField.set(
            BuiltInRegistries.ENCHANTMENT,
            IdentityHashMap<net.minecraft.world.item.enchantment.Enchantment,
                    Holder.Reference<net.minecraft.world.item.enchantment.Enchantment>>()
        )
    }

    override fun register(enchant: AiyatsbusEnchantmentBase): Enchantment {
        if (BuiltInRegistries.ENCHANTMENT.containsKey(CraftNamespacedKey.toMinecraft(enchant.enchantmentKey))) {
            val nms = BuiltInRegistries.ENCHANTMENT[CraftNamespacedKey.toMinecraft(enchant.enchantmentKey)]

            if (nms != null) {
                return AiyatsbusCraftEnchantment(enchant, nms)
            } else {
                throw IllegalStateException("Enchantment ${enchant.id} wasn't registered")
            }
        }

        Registry.register(BuiltInRegistries.ENCHANTMENT, enchant.id, VanillaAiyatsbusEnchantment(enchant.id))

        return register(enchant)
    }

    override fun unregister(enchant: AiyatsbusEnchantment) {

    }
}