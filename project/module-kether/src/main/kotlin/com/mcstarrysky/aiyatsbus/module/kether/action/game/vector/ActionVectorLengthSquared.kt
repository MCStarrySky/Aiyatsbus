package com.mcstarrysky.aiyatsbus.module.kether.action.game.vector

/**
 * Vulpecula
 * com.mcstarrysky.aiyatsbus.module.kether.action.vector
 *
 * @author Lanscarlos
 * @since 2023-03-22 15:26
 */
object ActionVectorLengthSquared : ActionVector.Resolver {

    override val name: Array<String> = arrayOf("length2", "size2")

    /**
     * vec length2 &vec
     * */
    override fun resolve(reader: ActionVector.Reader): ActionVector.Handler<out Any?> {
        return reader.handle {
            combine(
                source(),
            ) { vector ->
                vector.lengthSquared()
            }
        }
    }
}