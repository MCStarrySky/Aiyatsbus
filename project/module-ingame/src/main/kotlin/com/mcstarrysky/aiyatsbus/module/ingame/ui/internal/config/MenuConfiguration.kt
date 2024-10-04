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
package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config

import com.mcstarrysky.aiyatsbus.core.util.VariableReaders
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.oneOf
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.advance.KeywordConfiguration
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.advance.ShapeConfiguration
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.advance.TemplateConfiguration
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration
import taboolib.module.ui.type.PageableChest

@Suppress("MemberVisibilityCanBePrivate", "unused")
class MenuConfiguration(internal val source: Configuration) {

    val isDebug: Boolean = source.oneOf(*MenuSection.DEBUG.paths, getter = ConfigurationSection::getBoolean) ?: false

    val title: String? = source.oneOf(*MenuSection.TITLE.paths, getter = ConfigurationSection::getString)
    val shape: ShapeConfiguration = ShapeConfiguration(this)
    val templates: TemplateConfiguration = TemplateConfiguration(this)
    val keywords: KeywordConfiguration = KeywordConfiguration(this)
    val cached: MutableMap<String, Any?> = HashMap()
    val mapped: MutableMap<Int, Any?> = HashMap()

    fun title(vararg variables: Pair<String, () -> String>): String {
        return with(variables.toMap()) {
            VariableReaders.BRACES.replaceNested(title ?: MenuSection.TITLE.missing()) {
                get(this)?.invoke() ?: ""
            }
        }
    }

    fun setPreviousPage(menu: PageableChest<*>, keyword: String = "Previous") {
        shape[keyword].first().let { slot ->
            menu.setPreviousPage(slot) { _, it ->
                templates(keyword, slot, 0, !it)
            }
        }
    }

    fun setNextPage(menu: PageableChest<*>, keyword: String = "Next") {
        shape[keyword].first().let { slot ->
            menu.setNextPage(slot) { _, it ->
                templates(keyword, slot, 0, !it)
            }
        }
    }

    operator fun component1(): ShapeConfiguration = shape
    operator fun component2(): TemplateConfiguration = templates
    operator fun component3(): KeywordConfiguration = keywords
    operator fun component4(): MenuConfiguration = this
    operator fun component5(): MutableMap<String, Any?> = cached
    operator fun component6(): MutableMap<Int, Any?> = mapped

}