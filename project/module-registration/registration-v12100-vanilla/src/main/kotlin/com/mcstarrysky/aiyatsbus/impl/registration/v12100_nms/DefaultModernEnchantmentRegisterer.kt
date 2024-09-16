package com.mcstarrysky.aiyatsbus.impl.registration.v12100_nms

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentBase
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentManager
import com.mcstarrysky.aiyatsbus.core.registration.modern.ModernEnchantmentRegisterer
import com.mcstarrysky.aiyatsbus.impl.registration.v12100_paper.EnchantmentHelper
import net.minecraft.core.*
import net.minecraft.core.registries.Registries
import net.minecraft.resources.MinecraftKey
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_21_R1.CraftRegistry
import org.bukkit.craftbukkit.v1_21_R1.CraftServer
import org.bukkit.craftbukkit.v1_21_R1.enchantments.CraftEnchantment
import org.bukkit.craftbukkit.v1_21_R1.util.CraftNamespacedKey
import org.bukkit.enchantments.Enchantment
import taboolib.common.platform.PlatformFactory
import java.util.*
import java.util.function.BiFunction

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.registration.modern.DefaultModernEnchantmentRegisterer
 *
 * @author mical
 * @since 2024/2/17 17:28
 */
class DefaultModernEnchantmentRegisterer : ModernEnchantmentRegisterer {

    private val enchantmentRegistry = ((Bukkit.getServer() as CraftServer).server
        .registryAccess() as IRegistryCustom)
        .registryOrThrow(Registries.ENCHANTMENT)

    private val bukkitRegistry = org.bukkit.Registry.ENCHANTMENT

    private val frozenField = RegistryMaterials::class.java
        .declaredFields
        .filter { it.type.isPrimitive }[0]
        .apply { isAccessible = true }

    private val unregisteredIntrusiveHoldersField = RegistryMaterials::class.java
        .declaredFields
        .last { it.type == Map::class.java }
        .apply { isAccessible = true }

    private val minecraftToBukkit = bukkitRegistry::class.java
        .getDeclaredField("minecraftToBukkit")
        .apply { isAccessible = true }

    private val minecraftToBukkitAlt = CraftRegistry::class.java
        .getDeclaredField("minecraftToBukkit")
        .apply { isAccessible = true }

    private val cache = CraftRegistry::class.java
        .getDeclaredField("cache")
        .apply { isAccessible = true }

    override fun replaceRegistry() {
        val api = PlatformFactory.getAPI<AiyatsbusEnchantmentManager>()

        val newRegistryMTB =
            BiFunction<NamespacedKey, NMSEnchantment, Enchantment> { key, registry ->
                val isVanilla = enchantmentRegistry.containsKey(CraftNamespacedKey.toMinecraft(key))
                val aiyatsbus = api.getEnchant(key)

                if (isVanilla) {
                    CraftEnchantment(key, registry)
                } else if (aiyatsbus != null) {
                    aiyatsbus as Enchantment
                } else null
            }

        // Update bukkit registry
        minecraftToBukkit.set(bukkitRegistry, newRegistryMTB)
        minecraftToBukkitAlt.set(bukkitRegistry, newRegistryMTB)

        // Clear the enchantment cache
        cache.set(bukkitRegistry, mutableMapOf<NamespacedKey, Enchantment>())

        // Unfreeze NMS registry
        frozenField.set(enchantmentRegistry, false)
        unregisteredIntrusiveHoldersField.set(enchantmentRegistry, IdentityHashMap<NMSEnchantment, Holder.c<NMSEnchantment>>())
    }

    override fun register(enchant: AiyatsbusEnchantmentBase): Enchantment {
        // Clear the enchantment cache
        cache.set(bukkitRegistry, mutableMapOf<NamespacedKey, Enchantment>())

        if (enchantmentRegistry.containsKey(CraftNamespacedKey.toMinecraft(enchant.enchantmentKey))) {
            val nms = enchantmentRegistry[CraftNamespacedKey.toMinecraft(enchant.enchantmentKey)]

            if (nms != null) {
                 return EnchantmentHelper.createCraftEnchantment(enchant, nms) as CraftEnchantment
            } else {
                throw IllegalStateException("Enchantment ${enchant.id} wasn't registered")
            }
        }

        val vanillaEnchantment = vanillaEnchantment(enchant)


        enchantmentRegistry.createIntrusiveHolder(vanillaEnchantment)

        IRegistry.register(
            enchantmentRegistry,
            MinecraftKey.withDefaultNamespace(enchant.id),
            vanillaEnchantment
        )

        return register(enchant)
    }

    override fun unregister(enchant: AiyatsbusEnchantment) {

    }

    private fun vanillaEnchantment(enchant: AiyatsbusEnchantment): NMSEnchantment {
        val enchantment = NMSEnchantment.enchantment(
            NMSEnchantment.definition(
                HolderSet.empty(),
                1,
                enchant.basicData.maxLevel,
                NMSEnchantment.constantCost(1),
                NMSEnchantment.constantCost(1),
                0,
            )
        )
        return enchantment.build(MinecraftKey.withDefaultNamespace(enchant.id))
    }
}

typealias NMSEnchantment = net.minecraft.world.item.enchantment.Enchantment