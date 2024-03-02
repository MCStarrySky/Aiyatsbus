package com.mcstarrysky.aiyatsbus.module.custom.expansion

import com.google.gson.reflect.TypeToken
import com.mcstarrysky.aiyatsbus.core.AiyatsbusExpansionManager
import com.mcstarrysky.aiyatsbus.core.StandardPriorities
import com.mcstarrysky.aiyatsbus.core.util.GSON
import taboolib.common.ClassAppender
import taboolib.common.LifeCycle
import taboolib.common.PrimitiveIO
import taboolib.common.PrimitiveSettings
import taboolib.common.io.newFolder
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.registerLifeCycleTask
import java.io.File
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.Properties
import java.util.concurrent.ConcurrentHashMap
import java.util.jar.JarFile

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.custom.expansion.DefaultAiyatsbusExpansionManager
 *
 * @author mical
 * @since 2024/3/2 23:56
 */
class DefaultAiyatsbusExpansionManager : AiyatsbusExpansionManager {

    private val EXPANSION_INFOS = ConcurrentHashMap<String, AiyatsbusExpansionManager.ExpansionInfo>()
    private val EXPANSIONS = ConcurrentHashMap<String, AiyatsbusExpansion>()

    private val API = "http://r1.mcvps.vip:20003/"

    override fun loadExpansion(name: String) {
        newFolder(getDataFolder(), "expansions").listFiles { _, file -> file.endsWith(".jar") }?.firstOrNull {
            JarFile(it).use { jar ->
                val entry = jar.getJarEntry("expansion.properties")
                if (entry != null) {
                    val props = Properties()
                    props.load(jar.getInputStream(entry))
                    return@use props["name"].toString() == name
                }
                return@use false
            }
        }?.let {
            val loader = ClassAppender.addPath(it.toPath(), PrimitiveSettings.IS_ISOLATED_MODE, true)

            JarFile(it).use { jar ->
                val entry = jar.getJarEntry("expansion.properties")!!
                val props = Properties()
                props.load(jar.getInputStream(entry))
                val main = props["main"] as? String ?: return
                val version = props["version"] as String
                val author = props["author"] as String

                try {
                    val expansion = Class.forName(main, true, loader).newInstance() as AiyatsbusExpansion
                    expansion.onStarting()

                    EXPANSION_INFOS += name to AiyatsbusExpansionManager.ExpansionInfo.of(name, version, author)
                    EXPANSIONS += name to expansion
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
        }
    }

    override fun loadExpansions() {
        newFolder(getDataFolder(), "expansions").listFiles { _, file -> file.endsWith(".jar") }?.map {
            JarFile(it).use { jar ->
                val entry = jar.getJarEntry("expansion.properties")
                if (entry != null) {
                    val props = Properties()
                    props.load(jar.getInputStream(entry))
                    return@use props["name"].toString()
                }
                return@use null
            }
        }?.filterNotNull()?.forEach(this::loadExpansion)
    }

    override fun uninstallExpansion(name: String) {
        EXPANSIONS.remove(name)?.onStopping()
        EXPANSION_INFOS.remove(name)
    }

    override fun uninstallExpansions() {
        EXPANSIONS.forEach { (name, _) -> uninstallExpansion(name) }
    }

    override fun installExpansion(info: AiyatsbusExpansionManager.ExpansionInfo) {
        val folder = newFolder(getDataFolder(), "expansions")
        val file = File(folder, info.fileName)
        PrimitiveIO.downloadFile(URL(API + "download/${info.name}/${info.historyId}"), file)

        loadExpansion(info.name)
    }

    override fun getExpansions(): Map<String, AiyatsbusExpansionManager.ExpansionInfo> {
        return EXPANSION_INFOS
    }

    override fun fetchExpansionInfo(): List<AiyatsbusExpansionManager.ExpansionInfo> {
        return GSON.fromJson(
            URL(API).openStream().readBytes().toString(StandardCharsets.UTF_8),
            object : TypeToken<List<AiyatsbusExpansionManager.ExpansionInfo?>?>() {}.type
        )
    }

    init {
        registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.EXPANSIONS) { loadExpansions() }
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusExpansionManager>(DefaultAiyatsbusExpansionManager())
        }
    }
}