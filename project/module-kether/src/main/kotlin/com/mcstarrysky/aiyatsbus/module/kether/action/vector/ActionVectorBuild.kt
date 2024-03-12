package com.mcstarrysky.aiyatsbus.module.kether.action.vector

import taboolib.common.util.Vector

/**
 * Vulpecula
 * com.mcstarrysky.aiyatsbus.module.kether.action.vector
 *
 * @author Lanscarlos
 * @since 2023-03-22 14:52
 */
object ActionVectorBuild : ActionVector.Resolver {

    override val name: Array<String> = arrayOf("build", "create")

    /**
     * vec build &x &y &z
     * */
    override fun resolve(reader: ActionVector.Reader): ActionVector.Handler<out Any?> {
        return reader.transfer {
            combine(
                double(),
                double(),
                double()
            ) { x, y, z ->
                Vector(x, y, z)
            }
        }
    }
}