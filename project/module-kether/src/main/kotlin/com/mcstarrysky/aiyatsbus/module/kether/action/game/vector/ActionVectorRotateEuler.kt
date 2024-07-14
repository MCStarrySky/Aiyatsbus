package com.mcstarrysky.aiyatsbus.module.kether.action.game.vector

import taboolib.module.effect.utils.VectorUtils

/**
 * Vulpecula
 * com.mcstarrysky.aiyatsbus.module.kether.action.vector
 *
 * @author Lanscarlos
 * @since 2023-03-22 15:54
 */
object ActionVectorRotateEuler : ActionVector.Resolver {

    override val name: Array<String> = arrayOf("rotate-euler", "rotate-e")

    /**
     * vec rotate-euler &vec with/by &yaw &pitch
     * */
    override fun resolve(reader: ActionVector.Reader): ActionVector.Handler<out Any?> {
        return reader.transfer {
            combine(
                source(),
                trim("with", "by", then = float(0f)),
                float(0f)
            ) { vector, yaw, pitch ->
                if (yaw == 0f && pitch == 0f) return@combine vector
                VectorUtils.rotateVector(vector, yaw, pitch)
            }
        }
    }
}