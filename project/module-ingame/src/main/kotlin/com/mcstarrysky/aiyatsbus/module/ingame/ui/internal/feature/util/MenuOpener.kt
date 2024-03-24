package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature.util

import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.data.ActionContext

interface MenuOpener {

    val name: String

    fun open(context: ActionContext)

    operator fun invoke(context: ActionContext) = open(context)

}

@Suppress("unused")
class MenuOpenerBuilder(name: String? = null, builder: MenuOpenerBuilder.() -> Unit) : MenuOpener {

    override var name: String = name ?: ""
        internal set

    private var handler: (ActionContext) -> Unit = {
        throw NotImplementedError("未调用 onOpen 方法实现该打开方式")
    }

    init {
        builder()
    }

    fun onOpen(block: (ActionContext) -> Unit) {
        handler = block
    }

    override fun open(context: ActionContext) = handler(context)

}