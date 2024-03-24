package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.advance

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuSection
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature.FunctionalFeature
import taboolib.common.platform.function.info

@Suppress("MemberVisibilityCanBePrivate", "unused")
class KeywordConfiguration(val holder: MenuConfiguration) {

    private val keywords: BiMap<String, Char> by lazy {
        HashBiMap.create<String, Char>().apply {
            holder.templates.forEach { char, (_, _, features) ->
                for (extra in features[FunctionalFeature.name] ?: return@forEach) {
                    runCatching {
                        val keyword = FunctionalFeature.keyword(extra)
                        require(keyword !in this) { "存在重复的 Functional 关键词: $keyword@${this[keyword]}, $char" }
                        this[keyword] = char
                    }.onFailure {
                        MenuSection.TEMPLATE incorrect ("获取字符 $char 对应模板的 Functional 关键词时遇到错误" to it)
                    }
                }
            }

            if (holder.isDebug) {
                info("已加载的关键词: $this")
            }
        }
    }

    operator fun get(slot: Int): String? = get(holder.shape[slot])

    operator fun get(char: Char): String? = keywords.inverse()[char]

    operator fun get(keyword: String): Char? = keywords[keyword]

    fun require(keyword: String): Char = get(keyword) ?: (MenuSection.TEMPLATE incorrect "缺少与 Functional 关键词 $keyword 相关联的模版")

}