package com.mcstarrysky.aiyatsbus.module.kether.action.game.vector

/**
 * Vulpecula
 * com.mcstarrysky.aiyatsbus.module.kether.action.vector
 *
 * @author Lanscarlos
 * @since 2023-03-22 15:48
 */
object ActionVectorNormalize : ActionVector.Resolver {

    override val name: Array<String> = arrayOf("normalize", "normal")

    /**
     * vec normal &vec
     * */
    override fun resolve(reader: ActionVector.Reader): ActionVector.Handler<out Any?> {
        return reader.transfer {
            combine(
                source()
            ) { vector ->
                vector.normalize()
            }
        }
    }
}