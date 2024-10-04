/*
 * This file is part of ParrotX, licensed under the MIT License.
 *
 *  Copyright (c) 2020 Legoshi
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
package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal

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
            MenuFeature::class.java.isAssignableFrom(instance.javaClass) -> MenuFeatures.register(instance as MenuFeature)
            MenuFunction::class.java.isAssignableFrom(instance.javaClass) -> MenuFunctions.register(instance as MenuFunction)
            MenuOpener::class.java.isAssignableFrom(instance.javaClass) -> MenuOpeners.register(instance as MenuOpener)
            VariableProvider::class.java.isAssignableFrom(instance.javaClass) -> VariableProviders.register(instance as VariableProvider)
        }

        cached.computeIfAbsent(clazz) compute@{
            if (!clazz.hasAnnotation(MenuComponent::class.java)) return@compute ""
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
