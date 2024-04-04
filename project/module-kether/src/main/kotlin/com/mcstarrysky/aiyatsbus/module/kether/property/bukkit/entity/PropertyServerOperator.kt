package com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity

import com.mcstarrysky.aiyatsbus.core.util.coerceBoolean
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusGenericProperty
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusProperty
import org.bukkit.permissions.ServerOperator
import taboolib.common.OpenResult

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.property.bukkit.entity.PropertyServerOperator
 *
 * @author yanshiqwq
 * @since 2024/4/4 10:41
 */
@AiyatsbusProperty(
    id = "server-operator",
    bind = ServerOperator::class
)
class PropertyServerOperator : AiyatsbusGenericProperty<ServerOperator>("server-operator") {

    override fun readProperty(instance: ServerOperator, key: String): OpenResult {
        val property: Any? = when (key) {
            "isOp", "op" -> instance.isOp
            else -> return OpenResult.failed()
        }
        return OpenResult.successful(property)
    }

    override fun writeProperty(instance: ServerOperator, key: String, value: Any?): OpenResult {
        when (key) {
            "isOp", "op" -> {
                instance.isOp = value?.coerceBoolean() ?: return OpenResult.successful()
            }
            else -> return OpenResult.failed()
        }
        return OpenResult.successful()
    }
}