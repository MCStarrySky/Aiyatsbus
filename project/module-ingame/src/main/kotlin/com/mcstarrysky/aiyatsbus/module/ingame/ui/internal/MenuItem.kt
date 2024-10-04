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
package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal

import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.data.ActionContext
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.data.BuildContext
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature.FunctionalFeature
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.registry.MenuFeatures
import taboolib.common.platform.function.warning
import taboolib.library.configuration.ConfigurationSection
import taboolib.library.xseries.XItemStack
import taboolib.module.chat.component
import taboolib.module.ui.ClickEvent
import taboolib.module.ui.Menu
import taboolib.platform.util.isAir
import taboolib.platform.util.modifyMeta

@Suppress("MemberVisibilityCanBePrivate", "UnstableApiUsage")
class MenuItem(
    val config: MenuConfiguration,
    val char: Char,
    val features: Multimap<String, Map<String, Any?>>,
    private val _icon: ItemStack
) {

    val icon: ItemStack
        get() = _icon.clone()

    constructor(config: MenuConfiguration, section: ConfigurationSection) : this(
        config,
        requireNotNull(section.name.firstOrNull()) { "无法获取模板的映射字符" },
        MultimapBuilder
            .treeKeys(String.CASE_INSENSITIVE_ORDER)
            .linkedListValues()
            .build<String, Map<String, Any?>>()
            .apply {
                section["feature"]?.let { feature ->
                    when (feature) {
                        is String -> put(FunctionalFeature.name, mapOf("keyword" to feature))
                        is List<*> -> feature.forEach { value ->
                            when (value) {
                                is String -> put(FunctionalFeature.name, mapOf("keyword" to value))
                                is Map<*, *> -> {
                                    val extra = value.mapKeys { (key, _) -> "$key" }
                                    val type = requireNotNull(extra["=="] ?: extra["type"]) {
                                        "未指定 Feature 类型"
                                    }.toString().lowercase()
                                    put(type, extra)
                                }

                                null -> return@forEach
                                else -> throw IllegalArgumentException("不支持的 Feature 配置格式: $value (${value.javaClass.canonicalName})")
                            }
                        }

                        else -> throw IllegalArgumentException("不支持的 Feature 配置格式: $feature (${feature.javaClass.canonicalName})")
                    }
                }
            },
        requireNotNull(section["material"]?.toString()) { "未指定 Material" }.let { material ->
            if (material.equals("AIR", true)) {
                ItemStack(Material.AIR)
            } else {
                try {
                    XItemStack.deserialize(section).let deserialize@{ item ->
                        if (item.isAir()) {
                            return@let item
                        }
                        item.modifyMeta<ItemMeta> {
                            setDisplayName(displayName.component().buildColored().toLegacyText())
                            lore = lore?.map { it.component().buildColored().toLegacyText() }
                            // displayName(displayName.component().buildColored().tabooToAdventure().decoration(TextDecoration.ITALIC, false))
                            // lore(lore?.map { it.component().buildColored().tabooToAdventure().decoration(TextDecoration.ITALIC, false) })
                        }
                    }
                } catch (ex: Throwable) {
                    throw IllegalArgumentException("无法获取模板的显示物品", ex)
                }
            }
        }
    )

    fun build(slot: Int, index: Int, args: Map<String, Any?>): ItemStack {
        var result = icon
        features.asMap().forEach { (type, extras) ->
            val feature = MenuFeatures[type] ?: return@forEach warning("模板 $char 配置了一项未知的 Feature: $type")
            extras.forEach { extra ->
                val context = BuildContext(config, extra, slot, index, result, args)
                try {
                    result = feature.build(context)
                } catch (ex: Throwable) {
                    warning("模版 $char 在通过 Feature 构建图标时遇到错误: $type")
                    warning("上下文: $context")
                    ex.printStackTrace()
                }
            }
        }
        return result
    }

    fun build(slot: Int, index: Int, args: MutableMap<String, Any?>.() -> Unit): ItemStack =
        build(slot, index, mutableMapOf<String, Any?>().apply(args))

    fun build(slot: Int, index: Int, vararg args: Pair<String, Any?>): ItemStack = build(slot, index, args.toMap())

    operator fun invoke(slot: Int, index: Int, args: MutableMap<String, Any?>.() -> Unit = {}): ItemStack =
        build(slot, index, args)

    fun handle(menu: Menu, event: ClickEvent, args: Map<String, Any?>) {
        features.asMap().forEach { (type, extras) ->
            val feature = MenuFeatures[type] ?: return@forEach warning("模板 $char 配置了一项未知的 Feature: $type")
            extras.forEach { extra ->
                val context = ActionContext(config, extra, menu, event, args)
                try {
                    feature.handle(context)
                } catch (ex: Throwable) {
                    warning("模版 $char 在通过 Feature 响应点击时遇到错误: $type")
                    warning("上下文: $context")
                    ex.printStackTrace()
                }
            }
        }
    }

    fun handle(menu: Menu, event: ClickEvent, args: MutableMap<String, Any?>.() -> Unit) =
        handle(menu, event, mutableMapOf<String, Any?>().apply(args))

    fun handle(menu: Menu, event: ClickEvent, vararg args: Pair<String, Any?>) = handle(menu, event, args.toMap())

    operator fun component1(): Char = char
    operator fun component2(): ItemStack = icon
    operator fun component3(): Multimap<String, Map<String, Any?>> = features

}