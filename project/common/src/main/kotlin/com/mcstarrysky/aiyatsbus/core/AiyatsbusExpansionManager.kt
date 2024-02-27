package com.mcstarrysky.aiyatsbus.core

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusExpansionManager
 *
 * @author mical
 * @since 2024/2/27 22:42
 */
interface AiyatsbusExpansionManager {

    /**
     * 加载扩展
     */
    fun loadExpansion(name: String)

    /**
     * 加载已有的所有扩展
     */
    fun loadExpansions()

    /**
     * 卸载扩展
     */
    fun uninstallExpansion(name: String)

    /**
     * 卸载已有的所有扩展
     */
    fun uninstallExpansions()

    /**
     * 安装在线扩展
     */
    fun installExpansions(info: ExpansionInfo)

    /**
     * 获取正在运行的所有扩展信息
     */
    fun getExpansionInfos(): List<ExpansionInfo>

    /**
     * 重载某一扩展
     */
    fun reloadExpansion(name: String) {
        uninstallExpansion(name)
        loadExpansion(name)
    }

    data class ExpansionInfo(val identifier: String, val author: String, val version: String) {

        constructor(identifier: String) : this(identifier, "", "")

        constructor(identifier: String, version: String): this(identifier, "", "")
    }
}