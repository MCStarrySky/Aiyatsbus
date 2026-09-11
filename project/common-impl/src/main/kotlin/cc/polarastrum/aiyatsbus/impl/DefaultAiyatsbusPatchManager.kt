package cc.polarastrum.aiyatsbus.impl

import cc.polarastrum.aiyatsbus.core.AiyatsbusPatch
import cc.polarastrum.aiyatsbus.core.AiyatsbusPatchManager
import cc.polarastrum.aiyatsbus.core.AiyatsbusPatchResult
import taboolib.common.LifeCycle
import taboolib.common.io.newFile
import taboolib.common.io.newFolder
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.configuration.Configuration
import java.io.File
import java.io.IOException

/**
 * 默认 Aiyatsbus 补丁管理器实现
 *
 * 补丁定义见 [AiyatsbusPatches]。应用补丁时，将插件 JAR 内 `enchants/` 下的资源文件
 * 复制到服务器的 `plugins/Aiyatsbus/enchants/` 目录；目标文件已存在时跳过，不覆盖服主修改。
 * 同时会把补丁声明的 event-mapping 从 JAR 内 `core/event-mapping.yml` 写入服务器现有文件；
 * 已存在的映射名同样跳过，不覆盖服主修改。
 *
 * @author mical
 * @since 2026/8/27
 */
class DefaultAiyatsbusPatchManager : AiyatsbusPatchManager {

    private val patches = AiyatsbusPatches.all

    override fun getPatches(): List<AiyatsbusPatch> {
        return patches
    }

    override fun getPatch(name: String): AiyatsbusPatch? {
        return patches.firstOrNull { it.name == name }
    }

    override fun applyPatch(name: String): AiyatsbusPatchResult {
        val patch = getPatch(name) ?: return AiyatsbusPatchResult(null)
        return applyPatch(patch)
    }

    override fun applyPatch(patch: AiyatsbusPatch): AiyatsbusPatchResult {
        val added = mutableListOf<String>()
        val skipped = mutableListOf<String>()
        val missing = mutableListOf<String>()

        val enchantsFolder = newFolder(getDataFolder(), "enchants")

        for (path in patch.addFiles) {
            val normalized = path.replace('\\', '/')
            val target = File(enchantsFolder, normalized)

            if (target.exists()) {
                skipped += normalized
                continue
            }

            val resourcePath = "enchants/$normalized"
            val stream = javaClass.classLoader.getResourceAsStream(resourcePath)
            if (stream == null) {
                missing += normalized
                continue
            }

            try {
                target.parentFile?.mkdirs()
                stream.use { input ->
                    target.outputStream().use { output -> input.copyTo(output) }
                }
                added += normalized
            } catch (_: IOException) {
                target.delete()
                missing += normalized
            }
        }

        val addedEventMappings = mutableListOf<String>()
        val skippedEventMappings = mutableListOf<String>()
        val missingEventMappings = mutableListOf<String>()

        if (patch.eventMappings.isNotEmpty()) {
            val jarStream = javaClass.classLoader.getResourceAsStream("core/event-mapping.yml")
            if (jarStream == null) {
                missingEventMappings += patch.eventMappings
            } else {
                val jarConf = jarStream.use { Configuration.loadFromInputStream(it) }
                val eventMappingFile = newFile(getDataFolder(), "core/event-mapping.yml", create = false)
                val existed = eventMappingFile.exists()
                val serverConf = if (existed) {
                    Configuration.loadFromFile(eventMappingFile)
                } else {
                    releaseResourceFile("core/event-mapping.yml")
                    Configuration.loadFromFile(eventMappingFile)
                }

                var dirty = false
                for (name in patch.eventMappings) {
                    if (serverConf.contains("mappings.$name")) {
                        // 文件原本不存在时，刚释放的完整 event-mapping 已包含这些映射，视为新增。
                        if (existed) skippedEventMappings += name else addedEventMappings += name
                        continue
                    }
                    val mapping = jarConf["mappings.$name"]
                    if (mapping == null) {
                        missingEventMappings += name
                        continue
                    }
                    serverConf["mappings.$name"] = mapping
                    addedEventMappings += name
                    dirty = true
                }
                if (dirty) {
                    serverConf.saveToFile(eventMappingFile)
                }
            }
        }

        return AiyatsbusPatchResult(
            patch,
            added,
            skipped,
            missing,
            addedEventMappings,
            skippedEventMappings,
            missingEventMappings
        )
    }

    companion object {

        /**
         * 在系统常量阶段注册补丁管理器。
         */
        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusPatchManager>(DefaultAiyatsbusPatchManager())
        }
    }
}
