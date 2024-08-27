package com.mcstarrysky.aiyatsbus.core.util

import com.mcstarrysky.aiyatsbus.core.util.*
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common.util.VariableReader
import taboolib.module.chat.colored
import taboolib.platform.util.modifyMeta

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.ui.internal.function.Item
 *
 * @author mical
 * @since 2024/2/18 12:42
 */

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