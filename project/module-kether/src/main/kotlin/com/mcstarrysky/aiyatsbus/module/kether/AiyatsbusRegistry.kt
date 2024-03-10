package com.mcstarrysky.aiyatsbus.module.kether

import com.mcstarrysky.aiyatsbus.module.kether.property.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.property.AiyatsbusProperty
import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.platform.Awake
import taboolib.common.platform.function.warning
import taboolib.module.kether.Kether
import java.util.function.Supplier

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusRegistry
 *
 * @author Lanscarlos
 * @since 2023-03-19 22:08
 */
@Awake
object AiyatsbusRegistry : ClassVisitor(0) {

    override fun visitStart(clazz: Class<*>, instance: Supplier<*>?) {
        registerProperty(clazz, instance)
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

    override fun getLifeCycle(): LifeCycle {
        return LifeCycle.LOAD
    }
}