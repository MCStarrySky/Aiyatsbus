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

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.platform.Awake
import taboolib.common.platform.function.warning
import taboolib.library.reflex.ClassField
import taboolib.library.reflex.ClassMethod
import taboolib.library.reflex.ReflexClass
import taboolib.module.configuration.Configuration
import java.util.function.Consumer

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Reloadable

@Awake
@Suppress("unused")
object Reloadables : ClassVisitor() {

    private val registered: Multimap<Any, Consumer<Any>> = HashMultimap.create()

    override fun getLifeCycle(): LifeCycle = LifeCycle.LOAD

    override fun visit(method: ClassMethod, owner: ReflexClass) {
        if (method.isAnnotationPresent(Reloadable::class.java)) {
            registered.put(findInstance(owner) ?: return) { method.invoke(it) }
        }
    }

    override fun visit(field: ClassField, owner: ReflexClass) {
        if (field.isAnnotationPresent(Reloadable::class.java)) {
            val ins = findInstance(owner) ?: return
            val type = field.fieldType
            when {
                Configuration::class.java.isAssignableFrom(type) ->
                    registered.put(ins) { (field.get(it) as? Configuration)?.reload() }
                else -> warning("Unknown reloadable field type: ${type.canonicalName}")
            }
        }
    }

    fun execute() {
        registered.asMap().forEach { (instance, methods) ->
            methods.forEach {
                it.accept(instance)
            }
        }
    }
}