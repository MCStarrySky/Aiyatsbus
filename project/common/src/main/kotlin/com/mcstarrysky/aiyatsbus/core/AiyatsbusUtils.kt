package com.mcstarrysky.aiyatsbus.core

import com.mcstarrysky.aiyatsbus.core.data.CheckType
import com.mcstarrysky.aiyatsbus.core.data.Group
import com.mcstarrysky.aiyatsbus.core.data.Rarity
import com.mcstarrysky.aiyatsbus.core.data.Target
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta
import taboolib.platform.util.modifyMeta

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusUtils
 *
 * @author mical
 * @since 2024/2/17 22:12
 */
/**
 * 将 Enchantment 转换为 AiyatsbusEnchantment
 * 奇妙的 Et 命名来自白熊, 是 enchant 的缩写
 */
val Enchantment.aiyatsbusEt: AiyatsbusEnchantment?
    get() = Aiyatsbus.api().getEnchantmentManager().getByID(key.key)

fun aiyatsbusEt(identifier: String) = with(Aiyatsbus.api().getEnchantmentManager()) {
    getByName(identifier) ?: getByID(identifier)
}

fun aiyatsbusEt(key: NamespacedKey) = aiyatsbusEt(key.key)

fun aiyatsbusEts(rarity: Rarity) = Aiyatsbus.api().getEnchantmentManager().getByIDs().values.filter { it.rarity == rarity }

fun AiyatsbusEnchantment.book(level: Int = basicData.maxLevel) = ItemStack(Material.ENCHANTED_BOOK).modifyMeta<ItemMeta> { addEt(this@book, level) }

fun ItemStack.etsAvailable(
    checkType: CheckType = CheckType.ANVIL,
    player: Player? = null
) = Aiyatsbus.api().getEnchantmentManager().getByIDs().values.filter { it.limitations.checkAvailable(checkType, this, player).first }

/**
 * 从物品元数据获取附魔并自动转换为 AiyatsbusEnchantment
 */
var ItemMeta.fixedEnchants: Map<AiyatsbusEnchantment, Int>
    get() = (if (this is EnchantmentStorageMeta) storedEnchants else enchants).mapNotNull { (enchant, level) -> (enchant.aiyatsbusEt ?: return@mapNotNull null) to level }.toMap()
    set(value) {
        clearEts()
        if (this is EnchantmentStorageMeta) value.forEach { (enchant, level) -> addStoredEnchant(enchant.enchantment, level, true) }
        else value.forEach { (enchant, level) -> addEnchant(enchant.enchantment, level, true) }
    }

/**
 * 从物品获取附魔并自动转换为 AiyatsbusEnchantment
 */
var ItemStack?.fixedEnchants: Map<AiyatsbusEnchantment, Int>
    get() = this?.itemMeta?.fixedEnchants ?: emptyMap()
    set(value) { this?.modifyMeta<ItemMeta> { fixedEnchants = value } }

/**
 * 获取附魔等级, 若不存在则为 -1
 */
fun ItemMeta.etLevel(enchant: AiyatsbusEnchantment): Int {
    return fixedEnchants[enchant.enchantment as AiyatsbusEnchantment] ?: -1
}

/**
 * 获取附魔等级, 若不存在则为 -1
 */
fun ItemStack.etLevel(enchant: AiyatsbusEnchantment) = itemMeta.etLevel(enchant)

/**
 * 添加附魔
 */
fun ItemMeta.addEt(enchant: AiyatsbusEnchantment, level: Int = enchant.basicData.maxLevel) {
    removeEt(enchant)
    if (this is EnchantmentStorageMeta) addStoredEnchant(enchant.enchantment, level, true)
    else addEnchant(enchant.enchantment, level, true)
}

/**
 * 添加附魔
 */
fun ItemStack.addEt(enchant: AiyatsbusEnchantment, level: Int = enchant.basicData.maxLevel) {
    modifyMeta<ItemMeta> { addEt(enchant, level) }
}

/**
 * 删除附魔
 */
fun ItemMeta.removeEt(enchant: AiyatsbusEnchantment) {
    if (this is EnchantmentStorageMeta) removeStoredEnchant(enchant.enchantment)
    else removeEnchant(enchant.enchantment)
}

/**
 * 删除附魔
 */
fun ItemStack.removeEt(enchant: AiyatsbusEnchantment) {
    modifyMeta<ItemMeta> { removeEt(enchant) }
}

/**
 * 清除物品的所有附魔
 */
fun ItemMeta.clearEts() {
    if (this is EnchantmentStorageMeta) storedEnchants.forEach { removeStoredEnchant(it.key) }
    else enchants.forEach { removeEnchant(it.key) }
}

fun Material.isInTarget(target: Target?): Boolean = target?.types?.contains(this) ?: false

val Material.belongedTargets get() = Target.targets.values.filter(::isInTarget)

val Material.capability get() = belongedTargets.minOfOrNull { it.capability } ?: 32

fun Enchantment.isInGroup(name: String): Boolean = isInGroup(Group.groups[name])

fun Enchantment.isInGroup(group: Group?): Boolean = group?.enchantments?.find { it.enchantmentKey == key } != null

var Player.menuMode
    get() = Aiyatsbus.api().getPlayerDataHandler().get(this).menuMode
    set(value) {
        Aiyatsbus.api().getPlayerDataHandler().get(this).menuMode = value
    }

val Player.favorites get() = Aiyatsbus.api().getPlayerDataHandler().get(this).favorites

val Player.filters get() = Aiyatsbus.api().getPlayerDataHandler().get(this).filters

val Player.cooldown get() = Aiyatsbus.api().getPlayerDataHandler().get(this).cooldown