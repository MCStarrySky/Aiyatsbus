package com.mcstarrysky.aiyatsbus.core.util.inject

import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.platform.Awake
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.library.reflex.ClassMethod
import taboolib.library.reflex.ReflexClass

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.inject.AwakePriority
 *
 * @author mical
 * @since 2024/7/23 11:59
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class AwakePriority(val value: LifeCycle = LifeCycle.CONST, val priority: Int = 0)

@Awake
class AwakePriorityLoader : ClassVisitor(0) {

    override fun visit(method: ClassMethod, owner: ReflexClass) {
        if (method.isAnnotationPresent(AwakePriority::class.java)) {
            val instance = findInstance(owner)
            val lifeCycle = method.getAnnotation(AwakePriority::class.java).enum("value", LifeCycle.CONST)
            val priority = method.getAnnotation(AwakePriority::class.java).property("priority", 0)
            registerLifeCycleTask(lifeCycle, priority) {
                if (instance != null) {
                    method.invoke(instance)
                } else {
                    method.invokeStatic()
                }
            }
        }
    }

    override fun getLifeCycle(): LifeCycle {
        return LifeCycle.CONST
    }
}
