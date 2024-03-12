package com.mcstarrysky.aiyatsbus.module.kether.action.vector

/**
 * Vulpecula
 * com.mcstarrysky.aiyatsbus.module.kether.action.vector
 *
 * @author Lanscarlos
 * @since 2023-03-22 15:28
 */
object ActionVectorMidpoint : ActionVector.Resolver {

    override val name: Array<String> = arrayOf("midpoint")

    /**
     * vec midpoint &vec with/by &target
     * */
    override fun resolve(reader: ActionVector.Reader): ActionVector.Handler<out Any?> {
        val source = reader.source().accept(reader)

        return reader.transfer {
            if (expectToken("with")) {
                combine(
                    source,
                    vector(display = "vector target")
                ) { vector, target ->
                    // 返回新的 vector 对象
                    vector.getMidpoint(target)
                }
            } else if (expectToken("by")) {
                combine(
                    source,
                    vector(display = "vector target")
                ) { vector, target ->
                    // 返回自身 vector 对象
                    vector.midpoint(target)
                }
            } else {
                error("Unknown symbol \"${nextToken()}\" at vector midpoint action.")
            }
        }
    }
}