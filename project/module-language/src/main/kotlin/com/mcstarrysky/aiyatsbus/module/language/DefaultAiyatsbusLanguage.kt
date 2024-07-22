package com.mcstarrysky.aiyatsbus.module.language

import com.mcstarrysky.aiyatsbus.core.AiyatsbusLanguage
import com.mcstarrysky.aiyatsbus.core.asLangOrNull
import org.bukkit.command.CommandSender
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
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
        sender.sendLang(key, *args, sender.asLangTextOrNull("prefix") to "prefix")
    }

    override fun getLang(sender: CommandSender, key: String, vararg args: Any): String {
        return sender.asLangText(key, *args, sender.asLangTextOrNull("prefix") to "prefix")
    }

    override fun getLangOrNull(sender: CommandSender, key: String, vararg args: Any): String? {
        return sender.asLangTextOrNull(key, *args, sender.asLangTextOrNull("prefix") to "prefix")
    }

    override fun getLangList(sender: CommandSender, key: String, vararg args: Any): List<String> {
        return sender.asLangTextList(key, *args, sender.asLangTextOrNull("prefix") to "prefix")
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            // 注册服务
            PlatformFactory.registerAPI<AiyatsbusLanguage>(DefaultAiyatsbusLanguage())
            // 设置语言文件路径
            Language.path = "core/lang"
            // 设置语言文件释放路径
            Language.releasePath = "plugins/{0}/core/lang/{1}"
            // 启用行内复合文本支持
            Language.enableSimpleComponent = true
        }
    }
}