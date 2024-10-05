/*
 * This file is part of Adyeshach, licensed under the MIT License.
 *
 *  Copyright (c) 2020 TabooLib
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.mcstarrysky.aiyatsbus.core

import taboolib.common.LifeCycle
import taboolib.common.io.newFolder
import taboolib.common.platform.Awake
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.warning
import taboolib.common.util.resettableLazy
import taboolib.common.util.unsafeLazy
import taboolib.module.kether.Workspace
import taboolib.module.kether.printKetherErrorMessage

/**
 * Adyeshach
 * ink.ptms.adyeshach.impl.DefaultScriptManager
 *
 * @author 坏黑
 * @since 2022/12/17 21:58
 */
object ScriptWorkspace {

    /** 脚本工作空间 */
    val workspace by unsafeLazy { Workspace(newFolder(getDataFolder(), "script"), ".aiy", listOf("aiyatsbus")) }

    /** 脚本加载器 */
    val workspaceLoader by resettableLazy {
        try {
            workspace.loadAll()
        } catch (e: Exception) {
            warning("An error occurred while loading the script")
            e.printKetherErrorMessage()
        }
    }

    @Awake(LifeCycle.ACTIVE)
    private fun active() {
        workspaceLoader
    }
}