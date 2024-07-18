package com.mcstarrysky.aiyatsbus.core.util

import taboolib.common5.FileWatcher
import java.io.File

object FileWatcher {

    private val fileWatcher = FileWatcher()
    private val watching = LinkedHashSet<File>()

    /**
     * 监听文件改动
     */
    fun File.watch(callback: (File) -> Unit) {
        if (!fileWatcher.hasListener(this)) {
            fileWatcher.addSimpleListener(this) {
                callback(this)
            }
        }
    }

    /**
     * 取消监听
     */
    fun File.unwatch() {
        if (fileWatcher.hasListener(this)) {
            fileWatcher.removeListener(this)
        }
    }

    /**
     * 检测文件是否正在被监听器处理
     */
    var File.isProcessingByWatcher: Boolean
        get() = this in watching && fileWatcher.hasListener(this)
        set(value) {
            if (value) watching += this else watching -= this
        }
}