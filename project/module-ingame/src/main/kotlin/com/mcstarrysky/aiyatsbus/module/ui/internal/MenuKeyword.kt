package com.mcstarrysky.aiyatsbus.module.ui.internal

@Suppress("MemberVisibilityCanBePrivate")
data class MenuKeyword(val content: String, val indicator: String, val keyword: String) {

    constructor(content: String, delimiter: Char = DEFAULT_DELIMITER) : this(
        content,
        content.substringBefore(delimiter),
        content.substringAfter(delimiter, "")
    )

    companion object {
        var DEFAULT_DELIMITER: Char = ':'
        val EMPTY_KEYWORD: MenuKeyword by lazy { MenuKeyword("", "", "") }
        private val cached: MutableMap<String, MenuKeyword> = HashMap()

        fun of(content: String?, delimiter: Char = DEFAULT_DELIMITER): MenuKeyword {
            return content?.let { cached.computeIfAbsent(it) { _ -> MenuKeyword(it, delimiter) } } ?: EMPTY_KEYWORD
        }
    }
}
