package com.mcstarrysky.aiyatsbus.module.kether.action.game.vector

/**
 * Vulpecula
 * com.mcstarrysky.aiyatsbus.module.kether.action.vector
 *
 * @author Lanscarlos
 * @since 2023-03-22 15:30
 */
object ActionVectorModify : ActionVector.Resolver {

    override val name: Array<String> = arrayOf("modify", "set")

    override fun resolve(reader: ActionVector.Reader): ActionVector.Handler<out Any?> {
        val source = reader.source().accept(reader)

        return reader.transfer {
            if (peekToken().matches("-?\\d+(.\\d+)?".toRegex())) {
                /*
                * 数字
                * vec modify &vec x y z
                * */
                combine(
                    source,
                    double(0.0), // x
                    double(0.0), // y
                    double(0.0)  // z
                ) { vector, x, y, z ->
                    vector.x = x
                    vector.y = y
                    vector.z = z
                    vector
                }
            } else {
                /*
                * 参数
                * vec add &vec -x &x -y &y -z &z
                * */
                combine(
                    source,
                    argument("x", then = double()),
                    argument("y", then = double()),
                    argument("z", then = double()),
                ) { vector, x, y, z ->
                    vector.x = x ?: vector.x
                    vector.y = y ?: vector.y
                    vector.z = z ?: vector.z
                    vector
                }
            }
        }
    }
}