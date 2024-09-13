package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal

import com.mcstarrysky.aiyatsbus.core.util.assignableFrom
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature.util.*
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.registry.MenuFeatures
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.registry.MenuFunctions
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.registry.MenuOpeners
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.registry.VariableProviders
import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.platform.Awake
import taboolib.library.reflex.ClassField
import taboolib.library.reflex.ReflexClass

@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MenuComponent(val name: String = "")

@Awake
internal object MenuComponentRegister : ClassVisitor() {

    private val cached: MutableMap<ReflexClass, String> = HashMap()

    override fun getLifeCycle(): LifeCycle = LifeCycle.LOAD

    override fun visitStart(clazz: ReflexClass) {
        val instance = findInstance(clazz) ?: return

        when {
            MenuFeature::class.java.assignableFrom(clazz) -> MenuFeatures.register(instance as MenuFeature)
            MenuFunction::class.java.assignableFrom(clazz) -> MenuFunctions.register(instance as MenuFunction)
            MenuOpener::class.java.assignableFrom(clazz) -> MenuOpeners.register(instance as MenuOpener)
            VariableProvider::class.java.assignableFrom(clazz) -> VariableProviders.register(instance as VariableProvider)
        }

        cached.computeIfAbsent(clazz) compute@{
            val annotation = clazz.getAnnotation(MenuComponent::class.java)
            buildString {
                append(annotation.property<String>("name")?.ifBlank(it::simpleName))
                append(MenuKeyword.DEFAULT_DELIMITER)
            }
        }
    }

    override fun visit(field: ClassField, owner: ReflexClass) {
        val instance = findInstance(owner)
        if (instance == null || !field.isAnnotationPresent(MenuComponent::class.java)) {
            return
        }
        val name = field.getAnnotation(MenuComponent::class.java).property<String>("name")?.takeIf(String::isNotBlank) ?: field.name
        val group = cached[owner] ?: return

        val fieldType = field.fieldType
        when {
            MenuFunction::class.java.isAssignableFrom(fieldType) -> {
                val function = field.get(instance) as MenuFunction
                if (function is MenuFunctionBuilder) {
                    function.name = "$group$name"
                }
                MenuFunctions.register(function)
            }

            MenuOpener::class.java.isAssignableFrom(fieldType) -> {
                val opener = field.get(instance) as MenuOpener
                if (opener is MenuOpenerBuilder) {
                    opener.name = "$group$name"
                }
                MenuOpeners.register(opener)
            }

            VariableProvider::class.java.isAssignableFrom(fieldType) -> {
                val provider = field.get(instance) as VariableProvider
                if (provider is VariableProviderBuilder) {
                    provider.name = "$group$name"
                }
                VariableProviders.register(provider)
            }
        }
    }
}
