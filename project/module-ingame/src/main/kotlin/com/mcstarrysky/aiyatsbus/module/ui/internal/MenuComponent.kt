package com.mcstarrysky.aiyatsbus.module.ui.internal

import com.mcstarrysky.aiyatsbus.module.ui.internal.feature.util.*
import com.mcstarrysky.aiyatsbus.module.ui.internal.registry.MenuFeatures
import com.mcstarrysky.aiyatsbus.module.ui.internal.registry.MenuFunctions
import com.mcstarrysky.aiyatsbus.module.ui.internal.registry.MenuOpeners
import com.mcstarrysky.aiyatsbus.module.ui.internal.registry.VariableProviders
import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.platform.Awake
import taboolib.library.reflex.ClassField
import java.util.function.Supplier

@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MenuComponent(val name: String = "")

@Awake
internal object MenuComponentRegister : ClassVisitor() {

    private val cached: MutableMap<Class<*>, String> = HashMap()

    override fun getLifeCycle(): LifeCycle = LifeCycle.LOAD

    override fun visitStart(clazz: Class<*>, instance: Supplier<*>?) {
        if (instance == null) {
            return
        }

        when {
            MenuFeature::class.java.isAssignableFrom(clazz) -> MenuFeatures.register(instance.get() as MenuFeature)
            MenuFunction::class.java.isAssignableFrom(clazz) -> MenuFunctions.register(instance.get() as MenuFunction)
            MenuOpener::class.java.isAssignableFrom(clazz) -> MenuOpeners.register(instance.get() as MenuOpener)
            VariableProvider::class.java.isAssignableFrom(clazz) -> VariableProviders.register(instance.get() as VariableProvider)
        }

        cached.computeIfAbsent(clazz) compute@{
            val annotation = clazz.getAnnotation(MenuComponent::class.java) ?: return@compute ""
            buildString {
                append(annotation.name.ifBlank(it::getSimpleName))
                append(MenuKeyword.DEFAULT_DELIMITER)
            }
        }
    }

    override fun visit(field: ClassField, clazz: Class<*>, instance: Supplier<*>?) {
        if (instance == null || !field.isAnnotationPresent(MenuComponent::class.java)) {
            return
        }
        val name = field.getAnnotation(MenuComponent::class.java).property<String>("name")?.takeIf(String::isNotBlank) ?: field.name
        val group = cached[clazz] ?: return

        val fieldType = field.fieldType
        when {
            MenuFunction::class.java.isAssignableFrom(fieldType) -> {
                val function = field.get(instance.get()) as MenuFunction
                if (function is MenuFunctionBuilder) {
                    function.name = "$group$name"
                }
                MenuFunctions.register(function)
            }

            MenuOpener::class.java.isAssignableFrom(fieldType) -> {
                val opener = field.get(instance.get()) as MenuOpener
                if (opener is MenuOpenerBuilder) {
                    opener.name = "$group$name"
                }
                MenuOpeners.register(opener)
            }

            VariableProvider::class.java.isAssignableFrom(fieldType) -> {
                val provider = field.get(instance.get()) as VariableProvider
                if (provider is VariableProviderBuilder) {
                    provider.name = "$group$name"
                }
                VariableProviders.register(provider)
            }
        }
    }

}
