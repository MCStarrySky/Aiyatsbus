package com.mcstarrysky.aiyatsbus.module.kether.action.game.item

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action.item
 *
 * @author Lanscarlos
 * @since 2023-03-24 16:23
 */
object ActionItemDestroy : ActionItem.Resolver {

    override val name: Array<String> = arrayOf("destroy")

    override fun resolve(reader: ActionItem.Reader): ActionItem.Handler<out Any?> {
        return reader.transfer {
            combine(
                source()
            ) { item ->
                item.also { it.amount = 0 }
            }
        }
    }
}