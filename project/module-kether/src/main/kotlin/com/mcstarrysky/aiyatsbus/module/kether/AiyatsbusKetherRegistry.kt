package com.mcstarrysky.aiyatsbus.module.kether

import taboolib.common.inject.ClassVisitor.findInstance
import taboolib.common.platform.function.warning
import taboolib.library.reflex.ClassMethod
import taboolib.library.reflex.ReflexClass
import taboolib.module.kether.Kether
import taboolib.module.kether.ScriptActionParser
import kotlin.reflect.KClass

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusRegistry
 *
 * @author Lanscarlos
 * @since 2023-03-19 22:08
 */
object AiyatsbusKetherRegistry : ClassInjector() {

    /**
     * 访问函数
     * */
    override fun visit(method: ClassMethod, owner: ReflexClass) {
        registerAction(method, owner)
    }

    /**
     * 访问类
     * */
    override fun visitStart(owner: ReflexClass) {
        registerProperty(owner)
    }

    /**
     * 函数式语句注册
     * */
    private fun registerAction(method: ClassMethod, owner: ReflexClass) {
        if (!method.isAnnotationPresent(AiyatsbusParser::class.java) || method.returnType != ScriptActionParser::class.java) {
            return
        }

        // 加载注解
        val annotation = method.getAnnotation(AiyatsbusParser::class.java)
        val value = annotation.property<Any>("value") ?: return

        // FIXME 奇怪的问题, annotation 为什么能获取到 List?
        val values = if (value is List<*>) value.toTypedArray() else value as Array<*>

        // 尝试获取实例
        val instance = findInstance(owner)

        // 获取语句对象
        val parser = if (instance != null) {
            method.invoke(instance) as ScriptActionParser<*>
        } else {
            method.invokeStatic() as ScriptActionParser<*>
        }

        // 注册语句 私有
        for (name in values) {
            Kether.scriptRegistry.registerAction("aiyatsbus", name.toString(), parser)
        }
    }

    /**
     * 类式属性注册
     * */
    private fun registerProperty(owner: ReflexClass) {
        if (!owner.hasAnnotation(AiyatsbusProperty::class.java)) {
            return
        }

//        if (!AiyatsbusGenericProperty::class.java.assignableFrom(owner)) {
//            return
//        }

        // 加载注解
        val annotation = owner.getAnnotation(AiyatsbusProperty::class.java)

        // 尝试获取实例
        val instance = findInstance(owner)

        // 获取属性对象
        val property = if (instance != null) {
            instance as AiyatsbusGenericProperty<*>
        } else try {
            // 尝试实例化
            owner.structure.owner.instance?.getDeclaredConstructor()?.newInstance() as AiyatsbusGenericProperty<*>
        } catch (ex: Exception) {
            warning("Property \"${owner.name}\" must have a empty constructor.")
            return
        }

        // 注册属性 私有
        Kether.registeredScriptProperty.computeIfAbsent(annotation.property<KClass<*>>("bind")!!.java) { HashMap() }[property.id] = property
    }

    /**
     * 供外部调用的属性注册函数
     */
    @JvmStatic
    fun <T : AiyatsbusGenericProperty<*>> registerProperty(instance: T) {
        val clazz = instance.javaClass
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