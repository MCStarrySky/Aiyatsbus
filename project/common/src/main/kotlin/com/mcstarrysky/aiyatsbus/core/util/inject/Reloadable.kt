package com.mcstarrysky.aiyatsbus.core.util.inject

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.platform.Awake
import taboolib.common.platform.function.warning
import taboolib.library.reflex.ClassField
import taboolib.library.reflex.ClassMethod
import taboolib.module.configuration.Configuration
import java.util.function.Consumer
import java.util.function.Supplier

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Reloadable

@Awake
@Suppress("unused")
object Reloadables : ClassVisitor() {

    private val registered: Multimap<Any, Consumer<Any>> = HashMultimap.create()

    override fun getLifeCycle(): LifeCycle = LifeCycle.LOAD

    override fun visit(method: ClassMethod, clazz: Class<*>, instance: Supplier<*>?) {
        if (method.isAnnotationPresent(Reloadable::class.java)) {
            registered.put(instance?.get() ?: return) { method.invoke(it) }
        }
    }

    override fun visit(field: ClassField, clazz: Class<*>, instance: Supplier<*>?) {
        if (field.isAnnotationPresent(Reloadable::class.java)) {
            val ins = instance?.get() ?: return
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