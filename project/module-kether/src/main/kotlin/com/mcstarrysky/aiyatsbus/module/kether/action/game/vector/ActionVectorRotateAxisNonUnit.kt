package com.mcstarrysky.aiyatsbus.module.kether.action.game.vector

/**
 * Vulpecula
 * com.mcstarrysky.aiyatsbus.module.kether.action.vector
 *
 * @author Lanscarlos
 * @since 2023-03-22 15:54
 */
object ActionVectorRotateAxisNonUnit : ActionVector.Resolver {

    override val name: Array<String> = arrayOf("rotate-axis-non-unit", "rotate-a-n")

    /**
     * vec rotate-axis-non-unit &vec with/by &angle
     * */
    override fun resolve(reader: ActionVector.Reader): ActionVector.Handler<out Any?> {
        return reader.transfer {
            combine(
                source(),
                trim("with", "by", then = vector(display = "vector axis")),
                double(0.0)
            ) { vector, axis, angle ->
                if (angle == 0.0) return@combine vector
                vector.rotateAroundNonUnitAxis(axis, angle)
            }
        }
    }
}