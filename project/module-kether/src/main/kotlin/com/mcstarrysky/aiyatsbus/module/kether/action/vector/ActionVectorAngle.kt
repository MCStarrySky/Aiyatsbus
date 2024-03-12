package com.mcstarrysky.aiyatsbus.module.kether.action.vector

/**
 * Vulpecula
 * com.mcstarrysky.aiyatsbus.module.kether.action.vector
 *
 * @author Lanscarlos
 * @since 2023-03-22 14:58
 */
object ActionVectorAngle : ActionVector.Resolver {

    override val name: Array<String> = arrayOf("angle")

    /**
     * vec angle &vec with/by &target
     * */
    override fun resolve(reader: ActionVector.Reader): ActionVector.Handler<out Any?> {
        return reader.handle {
            combine(
                source(),
                trim("with", "by", then = vector(display = "vector target"))
            ) { vector, target ->
                vector.angle(target)
            }
        }
    }
}