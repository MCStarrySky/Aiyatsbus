package com.mcstarrysky.aiyatsbus.core.util

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common.util.VariableReader
import taboolib.module.chat.colored
import taboolib.platform.util.isAir
import taboolib.platform.util.modifyMeta

/**
 * 物品在铁砧上的操作数
 */
var ItemStack.repairCost: Int
    get() = Aiyatsbus.api().getMinecraftAPI().getRepairCost(this)
    set(value) = Aiyatsbus.api().getMinecraftAPI().setRepairCost(this, value)

fun ItemStack.variables(reader: VariableReader = VariableReaders.BRACES, func: VariableFunction): ItemStack {
    return modifyMeta<ItemMeta> {
        setDisplayName(displayName.let {
            reader.replaceNested(it) { func.transfer(this)?.firstOrNull() ?: this }.colored()
        })
        lore = lore?.variables(reader, func)?.colored()
    }
}

fun ItemStack.variable(key: String, value: Collection<String>, reader: VariableReader = VariableReaders.BRACES): ItemStack {
    return variables(reader) { if (it == key) value else null }
}

fun ItemStack.singletons(reader: VariableReader = VariableReaders.BRACES, func: SingleVariableFunction): ItemStack {
    return variables(reader, func)
}

fun ItemStack.singleton(key: String, value: String, reader: VariableReader = VariableReaders.BRACES): ItemStack {
    return singletons(reader) { if (it == key) value else null }
}

/**
 * 判断物品是否为 null 或是空气方块
 */
val ItemStack?.isNull get() = this?.isAir ?: true

/**
 * 判断物品是否为附魔书
 */
val ItemStack.isEnchantedBook get() = itemMeta is EnchantmentStorageMeta

/**
 * 获取/修改物品显示名称
 */
var ItemStack.name
    get() = itemMeta?.displayName
    set(value) {
        modifyMeta<ItemMeta> { setDisplayName(value) }
    }

/**
 * 获取/修改物品耐久
 */
var ItemStack.damage
    get() = (itemMeta as? Damageable)?.damage ?: 0
    set(value) {
        modifyMeta<Damageable> { damage = value }
    }

/**
 * 物品最大耐久度
 * */
val ItemStack.maxDurability: Int
    get() = this.type.maxDurability.toInt()

/**
 * 物品耐久度
 * */
var ItemStack.dura: Int
    get() = this.maxDurability - damage
    set(value) {
        this.damage = this.maxDurability - value
    }