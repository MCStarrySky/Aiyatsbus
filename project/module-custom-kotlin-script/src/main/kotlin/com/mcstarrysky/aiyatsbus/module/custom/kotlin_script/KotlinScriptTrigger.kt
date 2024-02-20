package com.mcstarrysky.aiyatsbus.module.custom.kotlin_script

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.customization.CustomTrigger
import com.mcstarrysky.aiyatsbus.core.customization.TriggerType
import ink.ptms.artifex.Artifex
import ink.ptms.artifex.script.ScriptCompiled
import ink.ptms.artifex.script.ScriptRuntimeProperty
import org.bukkit.NamespacedKey
import taboolib.common.io.newFile
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.severe
import taboolib.common.util.unsafeLazy
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.module.configuration.Configuration
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.custom.kotlin_script.KotlinScriptTrigger
 *
 * @author mical
 * @since 2024/2/21 00:12
 */
class KotlinScriptTrigger(
    override val enchant: AiyatsbusEnchantment,
    override val config: Configuration, // 附魔的整个配置文件
    override val type: TriggerType
) : CustomTrigger(enchant, config, type) {

    private val isArtifexEnabled: Boolean by unsafeLazy {
        runCatching { Class.forName("ink.ptms.artifex.bukkitside.ArtifexBukkit") }.isSuccess
    }

    override fun initialize() {
        if (!isArtifexEnabled)
            throw UnsupportedOperationException("Loading enchantment (${enchant.basicData.name} trigger, which is kotlin script but Artifex is not installed!")
        val file = newFile(getDataFolder(), config["trigger.file"].toString(), create = false, folder = false)
        val scriptCompiled = Artifex.api().getScriptCompiler().compile {
            it.source(file)
            it.onFailure { ex ->
                severe("Error occurred while compiling enchantment (${enchant.basicData.name}) trigger!")
                ex.printStackTrace()
            }
        } ?: throw IllegalStateException("Failed to compile enchantment (${enchant.basicData.name}) trigger!")
        try {
            val meta = Artifex.api().getScriptMetaHandler().getScriptMeta(scriptCompiled)
            scriptCompiled.invoke(meta.name(), ScriptRuntimeProperty()) // 暂时想不到可以提供什么运行时参数...
            scripts += enchant.enchantmentKey to (file to scriptCompiled)
        } catch (ex: Throwable) {
            severe("Error occurred while loading enchantment (${enchant.basicData.name}) trigger!")
            ex.printStackTrace()
        }
    }

    companion object {

        private val scripts: ConcurrentHashMap<NamespacedKey, Pair<File, ScriptCompiled>> = ConcurrentHashMap()

        fun release() {
            // toMap() 是克隆一份原 Map 防止在遍历时修改原 Map
            // 感谢白熊的 Bukkit 编程杂谈, 我爱白熊 https://hamsteryds.github.io/noticements-in-bukkit/#%E2%97%8B-map%E9%81%8D%E5%8E%86%E6%97%B6%E4%B8%8D%E8%A6%81%E4%BF%AE%E6%94%B9
            scripts.toMap().values.forEach {
                val (file, _) = it
                // Artifex.api().getScriptHelper().releaseScript(file, console())

                // FIXME: Artifex 的 Bug, 重定向了导致无法传入 console(), 只能传 Artifex 的 console(), 差评
                Artifex.api().getScriptHelper().invokeMethod<Unit>(
                    "releaseScript",
                    file,
                    Class.forName("ink.ptms.artifex.taboolib.common.platform.function.AdapterKt")
                        .invokeMethod<Any>("console", isStatic = true),
                    true
                )
            }
        }
    }
}