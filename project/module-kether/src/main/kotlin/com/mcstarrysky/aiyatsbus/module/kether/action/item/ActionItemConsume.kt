package com.mcstarrysky.aiyatsbus.module.kether.action.item

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action.item
 *
 * @author Lanscarlos
 * @since 2023-03-24 16:16
 */
object ActionItemConsume : ActionItem.Resolver {

    override val name: Array<String> = arrayOf("consume")

    override fun resolve(reader: ActionItem.Reader): ActionItem.Handler<out Any?> {
        return reader.transfer {
            combine(
                source(),
                optional("with", "to", then = int(display = "amount"), def = 1)
            ) { item, amount ->
                item.amount = (item.amount - amount).coerceIn(0, item.type.maxStackSize)
                item
            }
        }
    }
}