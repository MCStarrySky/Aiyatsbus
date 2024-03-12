package com.mcstarrysky.aiyatsbus.module.kether.action.vector

/**
 * Vulpecula
 * com.mcstarrysky.aiyatsbus.module.kether.action.vector
 *
 * @author Lanscarlos
 * @since 2023-03-22 15:26
 */
object ActionVectorLength : ActionVector.Resolver {

    override val name: Array<String> = arrayOf("length", "size")

    /**
     * vec length &vec
     * */
    override fun resolve(reader: ActionVector.Reader): ActionVector.Handler<out Any?> {
        return reader.handle {
            combine(
                source(),
            ) { vector ->
                vector.length()
            }
        }
    }
}