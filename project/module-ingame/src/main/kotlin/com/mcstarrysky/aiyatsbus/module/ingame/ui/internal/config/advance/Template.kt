/*
 * This file is part of ParrotX, licensed under the MIT License.
 *
 *  Copyright (c) 2020 Legoshi
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.advance

import com.google.common.collect.HashMultimap
import com.mcstarrysky.aiyatsbus.core.util.SimpleRegistry
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.asMap
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.oneOf
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.MenuItem
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuSection
import taboolib.library.configuration.ConfigurationSection

@Suppress("MemberVisibilityCanBePrivate", "unused")
class TemplateConfiguration(val holder: MenuConfiguration) : SimpleRegistry<Char, MenuItem>(HashMap()) {

    override fun getKey(value: MenuItem): Char = value.char

    init {
        holder.source.oneOf(*MenuSection.TEMPLATE.paths, getter = ConfigurationSection::getConfigurationSection)
            ?.asMap { getConfigurationSection(it) }
            ?.forEach { (key, section) ->
                register {
                    MenuItem(holder, section)
                }.onFailure {
                    MenuSection.TEMPLATE incorrect ("加载 $key 时遇到错误" to it)
                }
            }
        registered.putIfAbsent(' ', MenuItem(holder, ' ', HashMultimap.create(), ItemStack(Material.AIR)))
    }

    operator fun get(slot: Int): MenuItem? = get(holder.shape[slot])

    operator fun get(keyword: String): MenuItem? = get(holder.keywords[keyword])

    fun require(char: Char): MenuItem = get(char) ?: (MenuSection.TEMPLATE incorrect "未配置字符 $char 对应的模板")

    fun require(slot: Int): MenuItem = get(slot) ?: (MenuSection.TEMPLATE incorrect "未配置字符 ${holder.shape[slot]}@$slot 对应的模板")

    fun require(keyword: String): MenuItem = get(keyword)!!

    operator fun invoke(
        keyword: String,
        slot: Int,
        index: Int,
        isFallback: Boolean = false,
        fallback: String = "Fallback",
        args: MutableMap<String, Any?>.() -> Unit = {}
    ): ItemStack {
        return if (isFallback) {
            get(fallback)?.invoke(slot, index, args) ?: ItemStack(Material.AIR)
        } else {
            require(keyword).invoke(slot, index, args)
        }
    }

}