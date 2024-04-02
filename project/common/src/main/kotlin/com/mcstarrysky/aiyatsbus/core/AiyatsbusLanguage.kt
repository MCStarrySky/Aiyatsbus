package com.mcstarrysky.aiyatsbus.core

import org.bukkit.command.CommandSender

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusLanguage
 *
 * @author mical
 * @since 2024/4/2 20:09
 */
interface AiyatsbusLanguage {

    /**
     * 发送语言文件
     */
    fun sendLang(sender: CommandSender, key: String, vararg args: Any)

    /**
     * 获取可空语言文件
     */
    fun getLangOrNull(sender: CommandSender, key: String, vararg args: Any): String?

    /**
     * 获取语言文件
     */
    fun getLang(sender: CommandSender, key: String, vararg args: Any): String

    /**
     * 获取语言文件
     */
    fun getLangList(sender: CommandSender, key: String, vararg args: Any): List<String>

    /**
     * 解析一种格式
     * <lang>command-subCommands-reload-usage
     *
     * 如果不是 <lang> 开头, 则返回替换过变量的原始文本
     */
    fun parseLang(sender: CommandSender, content: String, vararg args: Any): String

    /**
     * 解析一种格式
     * <lang>command-subCommands-reload-usage
     *
     * 如果不是 <lang> 开头, 则返回替换过变量的原始文本
     */
    fun parseLangList(sender: CommandSender, content: List<String>, vararg args: Any): List<String>
}