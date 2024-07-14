package com.mcstarrysky.aiyatsbus.module.kether.action.game.vector

/**
 * Vulpecula
 * com.mcstarrysky.aiyatsbus.module.kether.action.vector
 *
 * @author Lanscarlos
 * @since 2023-03-22 15:21
 */
object ActionVectorDistance : ActionVector.Resolver {

    override val name: Array<String> = arrayOf("distance", "dist")

    /**
     * vec distance &vec with/by &target
     * */
    override fun resolve(reader: ActionVector.Reader): ActionVector.Handler<out Any?> {
        return reader.handle {
            combine(
                source(),
                trim("with", "by", then = vector(display = "vector target"))
            ) { vector, target ->
                vector.distance(target)
            }
        }
    }
}