package com.mcstarrysky.aiyatsbus.module.kether.action.item

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import org.bukkit.entity.LivingEntity

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.item.ActionItemDamage
 *
 * @author mical
 * @since 2024/3/20 23:30
 */
object ActionItemDamage : ActionItem.Resolver {

    override val name: Array<String> = arrayOf("damage")

    /**
     * item damage &item to 1 by &entity
     * 考虑了耐久等附魔
     */
    override fun resolve(reader: ActionItem.Reader): ActionItem.Handler<out Any?> {
        return reader.transfer {
            combine(
                source(),
                trim("to", then = int(0)),
                trim("by", then = entity())
            ) { item, damage, entity ->
                Aiyatsbus.api().getMinecraftAPI().damageItemStack(item, damage, entity as LivingEntity)
            }
        }
    }
}