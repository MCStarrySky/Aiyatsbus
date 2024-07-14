package com.mcstarrysky.aiyatsbus.module.kether.action.game.vector

/**
 * Vulpecula
 * com.mcstarrysky.aiyatsbus.module.kether.action.vector
 *
 * @author Lanscarlos
 * @since 2023-03-22 15:30
 */
object ActionVectorDivide : ActionVector.Resolver {

    override val name: Array<String> = arrayOf("div", "divide")

    override fun resolve(reader: ActionVector.Reader): ActionVector.Handler<out Any?> {
        val source = reader.source().accept(reader)

        return reader.transfer {
            if (expectToken("with", "by")) {
                /*
                * 向量对象
                * vec div &vec with &target
                * vec div &vec with 0,0,0
                * */
                combine(
                    source,
                    vector(display = "vector target")
                ) { vector, target ->
                    vector.divide(target)
                }
            } else if (peekToken().matches("-?\\d+(.\\d+)?".toRegex())) {
                /*
                * 数字
                * vec div &vec x y z
                * */
                combine(
                    source,
                    double(1.0), // x
                    double(1.0), // y
                    double(1.0)  // z
                ) { vector, x, y, z ->
                    vector.x *= x
                    vector.y *= y
                    vector.z *= z
                    vector
                }
            } else {
                /*
                * 参数
                * vec div &vec -x &x -y &y -z &z
                * */
                combine(
                    source,
                    argument("x", then = double(), def = 1.0),
                    argument("y", then = double(), def = 1.0),
                    argument("z", then = double(), def = 1.0),
                ) { vector, x, y, z ->
                    vector.x *= x
                    vector.y *= y
                    vector.z *= z
                    vector
                }
            }
        }
    }
}