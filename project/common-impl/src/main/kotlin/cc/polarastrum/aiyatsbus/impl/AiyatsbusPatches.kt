package cc.polarastrum.aiyatsbus.impl

import cc.polarastrum.aiyatsbus.core.AiyatsbusPatch

/**
 * Aiyatsbus 内置补丁定义
 *
 * 每个版本在此处新增一个 [AiyatsbusPatch]。
 * [AiyatsbusPatch.name] 是补丁名（即 `/aiyatsbus patch <name>` 中的版本编号），
 * [AiyatsbusPatch.addFiles] 是该补丁需要新增的文件路径列表，相对于 `enchants` 目录。
 * [AiyatsbusPatch.eventMappings] 是该补丁需要新增的 event-mapping 映射名列表，
 * 这些名称必须已存在于插件 JAR 的 `core/event-mapping.yml` 中。
 *
 * @author mical
 * @since 2026/8/27
 */
object AiyatsbusPatches {

    /**
     * 全部补丁列表。
     */
    val all = listOf(
        // 示例补丁：请按实际版本和文件修改或增删。
        AiyatsbusPatch(
            name = "1.4.9-snapshot-7",
            addFiles = listOf(
                "Packet-Default/barb.yml",
                "Packet-Default/bash.yml",
                "Packet-Default/counter.yml",
                "Packet-Default/twinge.yml",
            )
        ),
        AiyatsbusPatch(
            name = "1.4.9-snapshot-9",
            addFiles = listOf(
                "Packet-Default/artisanship.yml",
                "Packet-Default/sword_beam.yml"
            )
        )
    )
}
