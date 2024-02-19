package com.mcstarrysky.aiyatsbus.module.ui.internal.config

import com.mcstarrysky.aiyatsbus.module.ui.internal.function.VariableReaders
import com.mcstarrysky.aiyatsbus.module.ui.internal.function.oneOf
import com.mcstarrysky.aiyatsbus.module.ui.internal.config.advance.KeywordConfiguration
import com.mcstarrysky.aiyatsbus.module.ui.internal.config.advance.ShapeConfiguration
import com.mcstarrysky.aiyatsbus.module.ui.internal.config.advance.TemplateConfiguration
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration
import taboolib.module.ui.type.PageableChest

@Suppress("MemberVisibilityCanBePrivate", "unused")
class MenuConfiguration(internal val source: Configuration) {

    val isDebug: Boolean by lazy { source.oneOf(*MenuSection.DEBUG.paths, getter = ConfigurationSection::getBoolean) ?: false }

    val title: String? by lazy { source.oneOf(*MenuSection.TITLE.paths, getter = ConfigurationSection::getString) }
    val shape: ShapeConfiguration by lazy { ShapeConfiguration(this) }
    val templates: TemplateConfiguration by lazy { TemplateConfiguration(this) }
    val keywords: KeywordConfiguration by lazy { KeywordConfiguration(this) }
    val cached: MutableMap<String, Any?> by lazy { HashMap() }
    val mapped: MutableMap<Int, Any?> by lazy { HashMap() }

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