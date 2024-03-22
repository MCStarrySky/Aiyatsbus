package com.mcstarrysky.aiyatsbus.core.util

import taboolib.common5.FileWatcher
import java.io.File

object FileWatcher {

    private val fileWatcher = FileWatcher()

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
        fileWatcher.removeListener(this)
    }
}