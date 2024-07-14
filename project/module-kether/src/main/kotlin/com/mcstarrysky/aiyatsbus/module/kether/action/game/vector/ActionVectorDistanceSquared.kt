package com.mcstarrysky.aiyatsbus.module.kether.action.game.vector

/**
 * Vulpecula
 * com.mcstarrysky.aiyatsbus.module.kether.action.vector
 *
 * @author Lanscarlos
 * @since 2023-03-22 15:23
 */
object ActionVectorDistanceSquared : ActionVector.Resolver {

    override val name: Array<String> = arrayOf("distance2", "dist2")

    /**
     * vec distance2 &vec with/by &target
     * */
    override fun resolve(reader: ActionVector.Reader): ActionVector.Handler<out Any?> {
        return reader.handle {
            combine(
                source(),
                trim("with", "by", then = vector(display = "vector target"))
            ) { vector, target ->
                vector.distanceSquared(target)
            }
        }
    }
}