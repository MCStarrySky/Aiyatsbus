package cc.polarastrum.aiyatsbus.core

/**
 * Aiyatsbus 补丁
 *
 * 描述一个附魔包补丁：补丁名由开发者自行定义，[addFiles] 为需要新增的文件路径列表，
 * 路径相对于 `enchants` 目录，例如 `Packet-Default/a.yml`；
 * [eventMappings] 为需要新增的 event-mapping 映射名列表，例如 `entity-damaged-by-other-any`；
 * 这些名称必须已存在于插件 JAR 的 `core/event-mapping.yml` 中。
 *
 * @author mical
 * @since 2026/8/27
 */
data class AiyatsbusPatch(
    val name: String,
    val addFiles: List<String> = emptyList(),
    val eventMappings: List<String> = emptyList()
)

/**
 * Aiyatsbus 补丁应用结果
 *
 * @property patch 被应用的补丁；若补丁不存在则为 null
 * @property addedFiles 本次实际新增的文件列表
 * @property skippedFiles 因目标文件已存在而跳过的文件列表
 * @property missingFiles 未能添加的文件列表（插件资源缺失或写入失败）
 * @property addedEventMappings 本次实际新增的事件映射名列表
 * @property skippedEventMappings 因目标事件映射已存在而跳过的列表
 * @property missingEventMappings 插件 event-mapping 中不存在的事件映射名列表
 */
data class AiyatsbusPatchResult(
    val patch: AiyatsbusPatch?,
    val addedFiles: List<String> = emptyList(),
    val skippedFiles: List<String> = emptyList(),
    val missingFiles: List<String> = emptyList(),
    val addedEventMappings: List<String> = emptyList(),
    val skippedEventMappings: List<String> = emptyList(),
    val missingEventMappings: List<String> = emptyList()
) {

    /**
     * 是否完整应用成功：补丁存在，且没有缺失的插件资源文件或事件映射。
     */
    val success: Boolean
        get() = patch != null && missingFiles.isEmpty() && missingEventMappings.isEmpty()
}

/**
 * Aiyatsbus 补丁管理器接口
 *
 * 负责补丁的查询和应用。补丁由插件开发者定义，服主通过命令手动应用。
 *
 * @author mical
 * @since 2026/8/27
 */
interface AiyatsbusPatchManager {

    /**
     * 获取全部补丁。
     */
    fun getPatches(): List<AiyatsbusPatch>

    /**
     * 根据补丁名获取补丁。
     *
     * @param name 补丁名
     * @return 补丁实例，若不存在则返回 null
     */
    fun getPatch(name: String): AiyatsbusPatch?

    /**
     * 根据补丁名应用补丁。
     *
     * @param name 补丁名
     * @return 补丁应用结果
     */
    fun applyPatch(name: String): AiyatsbusPatchResult

    /**
     * 应用补丁。
     *
     * @param patch 要应用的补丁
     * @return 补丁应用结果
     */
    fun applyPatch(patch: AiyatsbusPatch): AiyatsbusPatchResult
}
