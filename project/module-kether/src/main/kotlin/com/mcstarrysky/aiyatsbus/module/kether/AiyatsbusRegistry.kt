package com.mcstarrysky.aiyatsbus.module.kether

import taboolib.common.platform.function.warning
import taboolib.library.reflex.ClassMethod
import taboolib.module.kether.Kether
import taboolib.module.kether.ScriptActionParser
import java.util.function.Supplier

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusRegistry
 *
 * @author Lanscarlos
 * @since 2023-03-19 22:08
 */
object AiyatsbusRegistry : ClassInjector() {

    /**
     * 访问函数
     * */
    override fun visit(method: ClassMethod, clazz: Class<*>, supplier: Supplier<*>?) {
        registerAction(method, supplier)
    }

    /**
     * 访问类
     * */
    override fun visitStart(clazz: Class<*>, supplier: Supplier<*>?) {
        registerProperty(clazz, supplier)
    }

    /**
     * 函数式语句注册
     * */
    private fun registerAction(method: ClassMethod, instance: Supplier<*>?) {
        if (!method.isAnnotationPresent(AiyatsbusParser::class.java) || method.returnType != ScriptActionParser::class.java) {
            return
        }

        // 加载注解
        val annotation = method.getAnnotation(AiyatsbusParser::class.java)
        val value = annotation.property<Array<String>>("value") ?: return

        // 获取语句对象
        val parser = if (instance != null) {
            method.invoke(instance.get()) as ScriptActionParser<*>
        } else {
            method.invokeStatic() as ScriptActionParser<*>
        }

        // 注册语句 私有
        for (name in value) {
            Kether.scriptRegistry.registerAction("aiyatsbus", name, parser)
        }
    }

    /**
     * 类式属性注册
     * */
    private fun registerProperty(clazz: Class<*>, instance: Supplier<*>?) {
        if (!clazz.isAnnotationPresent(AiyatsbusProperty::class.java)) {
            return
        }

        if (!AiyatsbusGenericProperty::class.java.isAssignableFrom(clazz)) {
            return
        }

        // 加载注解
        val annotation = clazz.getAnnotation(AiyatsbusProperty::class.java)

        // 获取属性对象
        val property = if (instance != null) {
            instance.get() as AiyatsbusGenericProperty<*>
        } else try {
            // 尝试实例化
            clazz.getDeclaredConstructor().newInstance() as AiyatsbusGenericProperty<*>
        } catch (ex: Exception) {
            warning("Property \"${clazz.name}\" must have a empty constructor.")
            return
        }

        // 注册属性 私有
        Kether.registeredScriptProperty.computeIfAbsent(annotation.bind.java) { HashMap() }[property.id] = property
    }

    fun <T : AiyatsbusGenericProperty<*>> registerProperty(clazz: Class<T>, instance: T) {
        if (!clazz.isAnnotationPresent(AiyatsbusProperty::class.java)) {
            return
        }

        if (!AiyatsbusGenericProperty::class.java.isAssignableFrom(clazz)) {
            return
        }

        // 加载注解
        val annotation = clazz.getAnnotation(AiyatsbusProperty::class.java)

        // 注册属性 私有
        Kether.registeredScriptProperty.computeIfAbsent(annotation.bind.java) { HashMap() }[instance.id] = instance
    }
}