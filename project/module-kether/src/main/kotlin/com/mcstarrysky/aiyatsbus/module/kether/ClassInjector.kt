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