/*
 * This file is part of Vulpecula, licensed under the MIT License.
 *
 *  Copyright (c) 2018 Bkm016
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
package com.mcstarrysky.aiyatsbus.module.kether

import com.mcstarrysky.aiyatsbus.core.util.packageName
import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.platform.Awake
import taboolib.common5.cbool
import taboolib.library.reflex.ClassMethod
import taboolib.library.reflex.ReflexClass

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.internal
 *
 * @author Lanscarlos
 * @since 2022-12-17 18:41
 */
abstract class ClassInjector {

    val name: String = this::class.java.name

    val packageName: String = this::class.java.packageName()

    open fun visit(method: ClassMethod, owner: ReflexClass) {}

    open fun visitStart(owner: ReflexClass) {}

    @Awake(LifeCycle.LOAD)
    object BacikalBoot : ClassVisitor(0) {

        private val ketherRegistry = AiyatsbusKetherRegistry

        override fun getLifeCycle() = LifeCycle.LOAD

        override fun visit(method: ClassMethod, owner: ReflexClass) {
            if (!owner.packageName()?.startsWith(ketherRegistry.packageName).cbool) return
            ketherRegistry.visit(method, owner)
        }

        override fun visitStart(owner: ReflexClass) {
            if (!owner.packageName()?.startsWith(ketherRegistry.packageName).cbool) return
            ketherRegistry.visitStart(owner)

            val instance = findInstance(owner) ?: return

            // 加载注册类
            if (!ClassInjector::class.java.isAssignableFrom(instance.javaClass)) return
            if (ketherRegistry::class.java.isAssignableFrom(instance.javaClass)) return

            injectors += instance as? ClassInjector ?: return
        }
    }

    @Awake(LifeCycle.LOAD)
    companion object : ClassVisitor(1) {

        private val injectors = mutableListOf<ClassInjector>()

        override fun getLifeCycle() = LifeCycle.LOAD

        override fun visit(method: ClassMethod, owner: ReflexClass) {
            injectors.forEach {
                if (!owner.packageName()?.startsWith(it.packageName).cbool) return@forEach
                it.visit(method, owner)
            }
        }

        override fun visitStart(owner: ReflexClass) {
            injectors.forEach {
                if (!owner.packageName()?.startsWith(it.packageName).cbool) return@forEach
                it.visitStart(owner)
            }
        }

        fun Class<*>.packageName(): String {
            return `package`.name
        }
    }
}