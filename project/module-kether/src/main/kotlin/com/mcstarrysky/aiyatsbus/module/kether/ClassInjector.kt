package com.mcstarrysky.aiyatsbus.module.kether

import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.platform.Awake
import taboolib.library.reflex.ClassMethod
import java.util.function.Supplier

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

    open fun visit(method: ClassMethod, clazz: Class<*>, supplier: Supplier<*>?) {}

    open fun visitStart(clazz: Class<*>, supplier: Supplier<*>?) {}

    @Awake(LifeCycle.LOAD)
    object BacikalBoot : ClassVisitor(0) {

        private val ketherRegistry = AiyatsbusRegistry

        override fun getLifeCycle() = LifeCycle.LOAD

        override fun visit(method: ClassMethod, clazz: Class<*>, supplier: Supplier<*>?) {
            if (!clazz.packageName().startsWith(ketherRegistry.packageName)) return
            ketherRegistry.visit(method, clazz, supplier)
        }

        override fun visitStart(clazz: Class<*>, supplier: Supplier<*>?) {
            if (!clazz.packageName().startsWith(ketherRegistry.packageName)) return
            ketherRegistry.visitStart(clazz, supplier)

            // 加载注册类
            if (!ClassInjector::class.java.isAssignableFrom(clazz)) return
            if (ketherRegistry::class.java.isAssignableFrom(clazz)) return

            injectors += let {
                if (supplier?.get() != null) {
                    supplier.get()
                } else try {
                    clazz.getDeclaredConstructor().newInstance()
                } catch (e: Exception) {
                    null
                }
            } as? ClassInjector ?: return
        }
    }

    @Awake(LifeCycle.LOAD)
    companion object : ClassVisitor(1) {

        private val injectors = mutableListOf<ClassInjector>()

        override fun getLifeCycle() = LifeCycle.LOAD

        override fun visit(method: ClassMethod, clazz: Class<*>, supplier: Supplier<*>?) {
            injectors.forEach {
                if (!clazz.packageName().startsWith(it.packageName)) return@forEach
                it.visit(method, clazz, supplier)
            }
        }

        override fun visitStart(clazz: Class<*>, supplier: Supplier<*>?) {
            injectors.forEach {
                if (!clazz.packageName().startsWith(it.packageName)) return@forEach
                it.visitStart(clazz, supplier)
            }
        }

        fun Class<*>.packageName(): String {
            return `package`.name
        }
    }
}