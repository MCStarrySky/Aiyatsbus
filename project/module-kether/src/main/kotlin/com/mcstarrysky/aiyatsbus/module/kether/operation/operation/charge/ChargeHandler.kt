/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.mcstarrysky.aiyatsbus.module.kether.operation.operation.charge

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.event.AiyatsbusBowChargeEvent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import java.util.concurrent.ConcurrentHashMap

/**
 * @author 坏黑
 * @since 2024/4/15 15:20
 */
object ChargeHandler {

    /**
     * 存玩家的蓄力信息
     * 键为玩家名称，值为对应的 [ChargeInfo] 对象。
     */
    val chargeInfo = ConcurrentHashMap<String, AiyatsbusBowChargeEvent.ChargeInfo>()

    /**
     * 获取指定玩家的蓄力信息。
     *
     * @param player 要获取蓄力信息的玩家
     * @return 玩家的 [ChargeInfo] 对象，如果不存在则返回 null
     */
    operator fun get(player: Player): AiyatsbusBowChargeEvent.ChargeInfo? {
        return chargeInfo[player.name]
    }

    /**
     * 取消指定玩家的蓄力状态。
     *
     * @param player 要取消蓄力状态的玩家
     * @return 如果成功取消蓄力状态则返回 true，否则返回 false
     */
    fun cancel(player: Player): Boolean {
        val info = chargeInfo.remove(player.name)
        if (info != null) {
            info.stopTime = System.currentTimeMillis()
            setHandActive(player, false)
            return true
        }
        return false
    }

    /**
     * 此方法检查玩家手中的弓或弩是否可以进行蓄力操作。它会检查主手和副手的物品，
     * 并触发 AiyatsbusBowChargeEvent.Prepare 事件来确定是否允许蓄力。
     *
     * @param player 要检查的玩家
     * @return 如果玩家手中的弓或弩可以蓄力，则返回 EquipmentSlot.HAND 或 EquipmentSlot.OFF_HAND；否则返回 null
     */
    fun canCharge(player: Player): EquipmentSlot? {
        val itemInMainHand = player.inventory.itemInMainHand
        if (itemInMainHand.type == Material.BOW || itemInMainHand.type == Material.CROSSBOW) {
            if (AiyatsbusBowChargeEvent.Prepare(player, itemInMainHand, EquipmentSlot.HAND).fire().isAllowed) {
                return EquipmentSlot.HAND
            }
        }
        val itemInOffhand = player.inventory.itemInOffHand
        if (itemInOffhand.type == Material.BOW || itemInOffhand.type == Material.CROSSBOW) {
            if (AiyatsbusBowChargeEvent.Prepare(player, itemInOffhand, EquipmentSlot.OFF_HAND).fire().isAllowed) {
                return EquipmentSlot.OFF_HAND
            }
        }
        return null
    }

    /**
     * 更新玩家的手部活动状态
     *
     * @param player 要更新状态的玩家
     * @param isHandActive 是否激活手部状态
     */
    fun setHandActive(player: Player, isHandActive: Boolean) {
//        Adyeshach.api().getMinecraftAPI().getEntityOperator().updateEntityMetadata(
//            playersNearby(player),
//            player.entityId,
//            Adyeshach.api().getMinecraftAPI().getEntityMetadataHandler().buildMetadata(AdyHuman::class.java) {
//                it.isHandActive = isHandActive
//            }
//        )
        Aiyatsbus.api().getMinecraftAPI().setHandActive(player, isHandActive)
    }

    /**
     * 获取玩家附近的其他玩家列表
     *
     * @param player 中心玩家
     * @return 距离中心玩家 32 格以内的玩家列表
     */
    private fun playersNearby(player: Player): List<Player> {
        return player.world.players.filter { it.location.distance(player.location) <= 32 }
    }
}