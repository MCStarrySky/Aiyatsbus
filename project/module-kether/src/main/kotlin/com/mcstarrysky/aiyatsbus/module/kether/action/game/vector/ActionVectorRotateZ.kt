package com.mcstarrysky.aiyatsbus.module.kether.action.game.vector

/**
 * Vulpecula
 * com.mcstarrysky.aiyatsbus.module.kether.action.vector
 *
 * @author Lanscarlos
 * @since 2023-03-22 15:54
 */
object ActionVectorRotateZ : ActionVector.Resolver {

    override val name: Array<String> = arrayOf("rotate-z")

    /**
     * vec rotate-z &vec with/by &angle
     * */
    override fun resolve(reader: ActionVector.Reader): ActionVector.Handler<out Any?> {
        return reader.transfer {
            combine(
                source(),
                trim("with", "by", then = double(0.0))
            ) { vector, angle ->
                if (angle == 0.0) return@combine vector
                vector.rotateAroundZ(angle)
            }
        }
    }
}