package com.mcstarrysky.aiyatsbus.module.language

import com.mcstarrysky.aiyatsbus.core.AiyatsbusLanguage
import org.bukkit.command.CommandSender
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.util.replaceWithOrder
import taboolib.module.lang.Language
import taboolib.platform.util.asLangText
import taboolib.platform.util.asLangTextList
import taboolib.platform.util.asLangTextOrNull
import taboolib.platform.util.sendLang

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.language.DefaultAiyatsbusLanguage
 *
 * @author mical
 * @since 2024/4/2 20:20
 */
class DefaultAiyatsbusLanguage : AiyatsbusLanguage {

    override fun sendLang(sender: CommandSender, key: String, vararg args: Any) {
        sender.sendLang(key, *args)
    }

    override fun getLangOrNull(sender: CommandSender, key: String, vararg args: Any): String? {
        return sender.asLangTextOrNull(key, *args)
    }

    override fun getLang(sender: CommandSender, key: String, vararg args: Any): String {
        return sender.asLangText(key, *args)
    }

    override fun getLangList(sender: CommandSender, key: String, vararg args: Any): List<String> {
        return sender.asLangTextList(key, *args)
    }

    override fun parseLang(sender: CommandSender, content: String, vararg args: Any): String {
        if (!content.startsWith("<lang>")) {
            return content.replaceWithOrder(*args)
        }
        return getLang(sender, content.substring(6), *args) ?: content.substring(6).replaceWithOrder(*args)
    }

    override fun parseLangList(sender: CommandSender, content: List<String>, vararg args: Any): List<String> {
        val result = mutableListOf<String>()
        for (line in content) {
            if (!line.startsWith("<lang>")) {
                result.add(line.replaceWithOrder(*args))
            } else {
                result.addAll(getLangList(sender, line.substring(6), *args))
            }
        }
        return result
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            // 注册服务
            PlatformFactory.registerAPI<AiyatsbusLanguage>(DefaultAiyatsbusLanguage())
            // 设置语言文件路径
            Language.path = "core/lang"
            // 启用行内复合文本支持
            Language.enableSimpleComponent = true
        }
    }
}