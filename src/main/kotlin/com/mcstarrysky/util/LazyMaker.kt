package com.mcstarrysky.util

/**
 * 声明一个线程不安全的延迟加载对象
 *
 * @param initializer 初始化函数
 */
fun <T> unsafeLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)