package com.mcstarrysky.aiyatsbus.module.kether.action.game.vector

/**
 * Vulpecula
 * com.mcstarrysky.aiyatsbus.module.kether.action.vector
 *
 * @author Lanscarlos
 * @since 2023-03-22 15:03
 */
object ActionVectorCross : ActionVector.Resolver {

    override val name: Array<String> = arrayOf("cross")

    /**
     * vec cross &vec with/by &target
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
                    vector.getCrossProduct(target)
                }
            } else if (expectToken("by")) {
                combine(
                    source,
                    vector(display = "vector target")
                ) { vector, target ->
                    // 返回自身 vector 对象
                    vector.crossProduct(target)
                }
            } else {
                error("Unknown symbol \"${nextToken()}\" at vector cross action.")
            }
        }
    }
}