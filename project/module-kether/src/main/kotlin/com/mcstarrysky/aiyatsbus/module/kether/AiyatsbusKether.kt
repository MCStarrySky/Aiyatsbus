package com.mcstarrysky.aiyatsbus.module.kether

import taboolib.library.kether.QuestAction
import taboolib.module.kether.ScriptFrame
import taboolib.module.kether.printKetherErrorMessage
import java.util.concurrent.CompletableFuture

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.Aiyatsbus
 *
 * @author Lanscarlos
 * @since 2023-02-27 15:32
 */
class AiyatsbusKether {

    class Action<T>(val func: (ScriptFrame) -> CompletableFuture<T>) {
        fun run(frame: ScriptFrame): CompletableFuture<T> {
            return func(frame).exceptionally {
                it.printKetherErrorMessage()
                null
            }
        }
    }

    class Parser<T>(val action: Action<T>) {

        constructor(func: (ScriptFrame) -> CompletableFuture<T>) : this(Action(func))

        fun resolve(): QuestAction<T> {
            return object : QuestAction<T>() {
                override fun process(frame: ScriptFrame): CompletableFuture<T> {
                    return action.run(frame)
                }
            }
        }
    }
}