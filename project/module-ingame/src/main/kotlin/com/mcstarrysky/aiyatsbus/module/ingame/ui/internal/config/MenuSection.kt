package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config

internal enum class MenuSection(val display: String, vararg val paths: String) {
    DEBUG("调试模式", "debug", "dev"),
    TITLE("标题", "title", "heading"),
    SHAPE("布局", "shape", "layout"),
    TEMPLATE("模板", "templates", "template", "items", "item");

    val formatted: String by lazy { "$display(${paths.joinToString("/")})" }

    fun missing(): Nothing = throw IllegalArgumentException("GUI 配置缺失或无效: $formatted")

    infix fun incorrect(reason: String): Nothing = throw IllegalArgumentException("GUI 配置 $formatted 不正确: $reason")

    infix fun incorrect(context: Pair<String, Throwable>) {
        val (reason, cause) = context
        IllegalArgumentException("GUI 配置 $formatted 不正确: $reason", cause).printStackTrace()
    }
}