/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.mcstarrysky.aiyatsbus.core

import com.mcstarrysky.aiyatsbus.core.data.*
import com.mcstarrysky.aiyatsbus.core.data.registry.*
import com.mcstarrysky.aiyatsbus.core.data.registry.Target
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common.platform.ProxyCommandSender
import taboolib.common5.RandomList
import taboolib.platform.util.modifyMeta

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusUtils
 *
 * @author mical
 * @since 2024/2/17 22:12
 */

/**
 * 使用 AiyatsbusLanguage 发送语言文件
 */
fun CommandSender.sendLang(node: String, vararg args: Any) {
    Aiyatsbus.api().getLanguage().sendLang(this, node, *args)
}

/**
 * 使用 AiyatsbusLanguage 获取语言文件
 */
fun CommandSender.asLangOrNull(node: String, vararg args: Any): String? {
    return Aiyatsbus.api().getLanguage().getLangOrNull(this, node, *args)
}

/**
 * 使用 AiyatsbusLanguage 获取语言文件
 */
fun CommandSender.asLang(node: String, vararg args: Any): String {
    return Aiyatsbus.api().getLanguage().getLang(this, node, *args)
}

/**
 * 使用 AiyatsbusLanguage 获取语言文件
 */
fun CommandSender.asLangList(node: String, vararg args: Any): List<String> {
    return Aiyatsbus.api().getLanguage().getLangList(this, node, *args)
}

/**
 * 使用 AiyatsbusLanguage 发送语言文件
 */
fun ProxyCommandSender.sendLang(node: String, vararg args: Any) {
    cast<CommandSender>().sendLang(node, *args)
}

/**
 * 使用 AiyatsbusLanguage 获取语言文件
 */
fun ProxyCommandSender.asLangOrNull(node: String, vararg args: Any): String? {
    return cast<CommandSender>().asLangOrNull(node, *args)
}

/**
 * 使用 AiyatsbusLanguage 获取语言文件
 */
fun ProxyCommandSender.asLang(node: String, vararg args: Any): String {
    return cast<CommandSender>().asLang(node, *args)
}

/**
 * 使用 AiyatsbusLanguage 获取语言文件
 */
fun ProxyCommandSender.asLangList(node: String, vararg args: Any): List<String> {
    return cast<CommandSender>().asLangList(node, *args)
}

/**
 * 将 Enchantment 转换为 AiyatsbusEnchantment
 * 奇妙的 Et 命名来自白熊, 是 enchant 的缩写
 */
val Enchantment.aiyatsbusEt: AiyatsbusEnchantment
    get() = Aiyatsbus.api().getEnchantmentManager().getEnchant(key) ?: error("Enchantment ${key.key} not found. (Maybe it's a vanilla enchantment. Please ensure all of the vanilla enchantment's files are complete.)")

/**
 * 根据名称或 Key 获取 AiyatsbusEnchantment 附魔对象
 */
fun aiyatsbusEt(identifier: String) = with(Aiyatsbus.api().getEnchantmentManager()) {
    getByName(identifier) ?: getEnchant(identifier)
}

/**
 * 根据 Key 获取 AiyatsbusEnchantment 附魔对象
 */
fun aiyatsbusEt(key: NamespacedKey) = aiyatsbusEt(key.key)

/**
 * 获取某个品质对应的所有 AiyatsbusEnchantment 附魔
 */
fun aiyatsbusEts(rarity: Rarity) = Aiyatsbus.api().getEnchantmentManager().getEnchants().values.filter { it.rarity == rarity }

fun ItemStack.toDisplayMode(player: Player): ItemStack {
    return Aiyatsbus.api().getDisplayManager().display(this, player)
}

fun ItemStack.toRevertMode(player: Player): ItemStack {
    return Aiyatsbus.api().getDisplayManager().undisplay(this, player)
}

/**
 * 获取附魔的附魔书
 */
fun AiyatsbusEnchantment.book(level: Int = basicData.maxLevel) = ItemStack(Material.ENCHANTED_BOOK).modifyMeta<ItemMeta> { addEt(this@book, level) }

/**
 * 获取附魔的附魔书
 */
fun Enchantment.book(level: Int = maxLevel) = (this as AiyatsbusEnchantment).book(level)

/**
 * 获取该物品可用的所有附魔
 */
fun ItemStack.etsAvailable(
    checkType: CheckType = CheckType.ANVIL,
    player: Player? = null
): List<AiyatsbusEnchantment> = Aiyatsbus.api().getEnchantmentManager().getEnchants().values.filter { it.limitations.checkAvailable(checkType, this, player).isSuccess }

/**
 * 从特定品质中根据附魔权重抽取一个附魔
 */
fun Rarity.drawEt(): AiyatsbusEnchantment? = RandomList(*aiyatsbusEts(this).associateWith { it.alternativeData.weight }.toList().toTypedArray()).random()

/**
 * 从物品元数据获取附魔并自动转换为 AiyatsbusEnchantment
 */
var ItemMeta.fixedEnchants: Map<AiyatsbusEnchantment, Int>
    get() = (if (this is EnchantmentStorageMeta) storedEnchants else enchants).map { (enchant, level) -> enchant.aiyatsbusEt to level }.toMap()
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

/**
 * 清楚物品的所有附魔
 */
fun ItemStack.clearEts() {
    modifyMeta<ItemMeta> { clearEts() }
}

/**
 * 从特定附魔列表中根据品质和附魔的权重抽取一个附魔
 */
fun Collection<AiyatsbusEnchantment>.drawEt(): AiyatsbusEnchantment? {
    val rarity = RandomList(*associate { it.rarity to it.rarity.weight }.toList().toTypedArray()).random()
    return RandomList(*filter { rarity == it.rarity }.associateWith { it.alternativeData.weight }.toList().toTypedArray()).random()
}

/**
 * 检查某个物品是否属于某一类物品
 */
fun Material.isInTarget(target: Target?): Boolean = target?.types?.contains(this) ?: false

/**
 * 获取这个物品所属类别
 */
val Material.belongedTargets get() = aiyatsbusTargets.values.filter(::isInTarget)

/**
 * 获取这个物品所能附魔的最大附魔词条数
 */
val Material.capability get() = belongedTargets.minOfOrNull { it.capability } ?: 32

/**
 * 检查附魔是否处于某个分组
 */
fun Enchantment.isInGroup(name: String): Boolean = isInGroup(aiyatsbusGroup(name))

/**
 * 检查附魔是否处于某个分组
 */
fun Enchantment.isInGroup(group: Group?): Boolean = group?.enchantments?.find { it.enchantmentKey == key } != null

/**
 * 玩家的菜单类型
 */
var Player.menuMode
    get() = Aiyatsbus.api().getPlayerDataHandler().get(this).menuMode
    set(value) {
        Aiyatsbus.api().getPlayerDataHandler().get(this).menuMode = value
    }

/**
 * 玩家的收藏夹
 */
val Player.favorites get() = Aiyatsbus.api().getPlayerDataHandler().get(this).favorites

/**
 * 玩家的过滤器
 */
val Player.filters get() = Aiyatsbus.api().getPlayerDataHandler().get(this).filters

/**
 * 玩家的冷却列表
 */
val Player.cooldown get() = Aiyatsbus.api().getPlayerDataHandler().get(this).cooldown

/**
 * 所有分组
 */
val aiyatsbusGroups = GroupLoader.registered

/**
 * 获取分组
 */
fun aiyatsbusGroup(identifier: String): Group? = aiyatsbusGroups[identifier]

/**
 * 所有品质
 */
val aiyatsbusRarities = RarityLoader.registered

/**
 * 获取品质
 */
fun aiyatsbusRarity(identifier: String): Rarity? = aiyatsbusRarities[identifier] ?: aiyatsbusRarities.values.firstOrNull { it.name == identifier }

/**
 * 所有对象
 */
val aiyatsbusTargets = TargetLoader.registered

/**
 * 获取对象
 */
fun aiyatsbusTarget(identifier: String): Target? = aiyatsbusTargets[identifier] ?: aiyatsbusTargets.values.firstOrNull { it.name == identifier }