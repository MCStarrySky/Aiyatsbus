package com.mcstarrysky.aiyatsbus.module.kether.action.item

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action.item
 *
 * @author Lanscarlos
 * @since 2023-03-23 19:15
 */
object ActionItemAmount : ActionItem.Resolver {

    override val name: Array<String> = arrayOf("amount", "amt")

    override fun resolve(reader: ActionItem.Reader): ActionItem.Handler<out Any?> {
        val source = reader.source().accept(reader)

        reader.mark()
        return when (reader.nextToken()) {
            "add", "give" -> {
                reader.transfer {
                    combine(
                        source,
                        trim("to", then = int(0))
                    ) { item, amount ->
                        item.amount += amount
                        item
                    }
                }
            }
            "sub", "take" -> {
                reader.transfer {
                    combine(
                        source,
                        trim("to", then = int(0))
                    ) { item, amount ->
                        item.amount -= amount
                        item
                    }
                }
            }
            "set", "modify" -> {
                reader.transfer {
                    combine(
                        source,
                        trim("to", then = intOrNull())
                    ) { item, amount ->
                        item.amount = amount ?: item.amount
                        item
                    }
                }
            }
            "current", "cur" -> {
                reader.handle {
                    combine(source) { item -> item.amount }
                }
            }
            "max" -> {
                reader.handle {
                    combine(source) { item -> item.type.maxStackSize }
                }
            }
            else -> {
                reader.reset()
                // 默认返回数量
                reader.handle {
                    combine(source) { item -> item.amount }
                }
            }
        }
    }
}