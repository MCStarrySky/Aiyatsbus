package com.mcstarrysky.aiyatsbus.module.kether.action

import taboolib.library.kether.ParsedAction
import taboolib.module.kether.ScriptAction
import taboolib.module.kether.ScriptFrame
import taboolib.module.kether.run
import java.util.concurrent.CompletableFuture

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action
 *
 * @author Lanscarlos
 * @since 2023-03-20 00:16
 */
class ActionBlock(
    val block: List<ParsedAction<*>>
) : ScriptAction<Any?>() {

    override fun run(frame: ScriptFrame): CompletableFuture<Any?> {
        return if (block.isEmpty()) {
            CompletableFuture.completedFuture(null)
        } else if (block.size == 1) {
            frame.run(block[0])
        } else {
            var previous: CompletableFuture<Any?> = frame.run(block.first())
            for (i in 1 until block.size) {
                val current = block[i]
                previous = if (previous.isDone) {
                    frame.run(current)
                } else {
                    previous.thenCompose { frame.run(current) }
                }
            }
            previous
        }
    }
}