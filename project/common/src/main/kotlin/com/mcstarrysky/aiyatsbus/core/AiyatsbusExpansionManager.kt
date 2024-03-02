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
    fun installExpansion(info: ExpansionInfo)

    /**
     * 获取正在运行的所有扩展信息
     */
    fun getExpansions(): Map<String, ExpansionInfo>

    /**
     * 重载某一扩展
     */
    fun reloadExpansion(name: String) {
        uninstallExpansion(name)
        loadExpansion(name)
    }

    /**
     * 获取可下载使用的所有附魔
     */
    fun fetchExpansionInfo(): List<ExpansionInfo>

    data class ExpansionInfo(val name: String, val version: String, val author: String, val downloadCount: Long, val time: Long, val formattedTime: String, val description: String, val historyId: Int, val fileName: String) {

        companion object {

            fun of(identifier: String, version: String, author: String): ExpansionInfo {
                return ExpansionInfo(identifier, "", "", -1, -1, "", "", -2, "")
            }
        }
    }
}