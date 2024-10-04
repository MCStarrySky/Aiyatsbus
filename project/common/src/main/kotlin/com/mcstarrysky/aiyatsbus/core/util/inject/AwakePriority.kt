/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
