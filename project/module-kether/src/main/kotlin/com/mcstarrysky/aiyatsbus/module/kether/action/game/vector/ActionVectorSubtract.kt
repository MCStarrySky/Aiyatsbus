package com.mcstarrysky.aiyatsbus.module.kether.action.game.vector

/**
 * Vulpecula
 * com.mcstarrysky.aiyatsbus.module.kether.action.vector
 *
 * @author Lanscarlos
 * @since 2023-03-22 15:30
 */
object ActionVectorSubtract : ActionVector.Resolver {

    override val name: Array<String> = arrayOf("sub", "minus")

    override fun resolve(reader: ActionVector.Reader): ActionVector.Handler<out Any?> {
        val source = reader.source().accept(reader)

        return reader.transfer {
            if (expectToken("with", "by")) {
                /*
                * 向量对象
                * vec sub &vec with &target
                * vec sub &vec with 0,0,0
                * */
                combine(
                    source,
                    vector(display = "vector target")
                ) { vector, target ->
                    vector.subtract(target)
                }
            } else if (peekToken().matches("-?\\d+(.\\d+)?".toRegex())) {
                /*
                * 数字
                * vec sub &vec x y z
                * */
                combine(
                    source,
                    double(0.0), // x
                    double(0.0), // y
                    double(0.0)  // z
                ) { vector, x, y, z ->
                    vector.x -= x
                    vector.y -= y
                    vector.z -= z
                    vector
                }
            } else {
                /*
                * 参数
                * vec sub &vec -x &x -y &y -z &z
                * */
                combine(
                    source,
                    argument("x", then = double(), def = 0.0),
                    argument("y", then = double(), def = 0.0),
                    argument("z", then = double(), def = 0.0),
                ) { vector, x, y, z ->
                    vector.x -= x
                    vector.y -= y
                    vector.z -= z
                    vector
                }
            }
        }
    }
}