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
package com.mcstarrysky.aiyatsbus.core.event

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * 表示与 弓 蓄力相关的事件。
 *
 * @author 坏黑
 */
class AiyatsbusBowChargeEvent {

    /**
     * 表示 弓 蓄力准备阶段的事件。
     *
     * @property player 执行蓄力操作的玩家。
     * @property itemStack 正在蓄力的物品堆。
     * @property hand 玩家使用的装备槽位。
     */
    class Prepare(val player: Player, val itemStack: ItemStack, val hand: EquipmentSlot) : BukkitProxyEvent() {

        override val allowCancelled: Boolean
            get() = false

        /**
         * 表示是否允许玩家进行蓄力操作。
         */
        var isAllowed = false

        /**
         * 触发此事件。
         */
        fun fire(): Prepare {
            call()
            return this
        }
    }

    /**
     * 表示 弓 蓄力释放阶段的事件。
     *
     * @property player 释放蓄力的玩家。
     * @property itemStack 释放蓄力的物品堆。
     * @property chargeInfo 蓄力信息。
     */
    class Released(val player: Player, val itemStack: ItemStack, val hand: EquipmentSlot, val chargeInfo: ChargeInfo) : BukkitProxyEvent() {

        override val allowCancelled: Boolean
            get() = false

        /**
         * 蓄力开始的时间。
         */
        val startTime = chargeInfo.startTime

        /**
         * 蓄力持续的时间。
         */
        val chargeTime = chargeInfo.chargeTime
    }

    /**
     * 表示玩家在蓄力过程中被打断的事件。
     *
     * @property player 被打断蓄力的玩家。
     * @property chargeInfo 蓄力信息。
     * @property reason 打断蓄力的原因。
     * @property source 事件来源。
     */
    class Break(val player: Player, val chargeInfo: ChargeInfo, val reason: Reason, val source: Event?) : BukkitProxyEvent() {

        /**
         * 表示打断蓄力的原因。
         */
        enum class Reason {

            /**
             * 玩家受到伤害而被打断。
             */
            DAMAGED,

            /**
             * 玩家使用技能而被打断。
             */
            SKILL
        }

        /**
         * 蓄力开始的时间。
         */
        val startTime = chargeInfo.startTime

        /**
         * 蓄力持续的时间。
         */
        val chargeTime = chargeInfo.chargeTime
    }

    /**
     * 表示玩家蓄力信息的数据类。
     *
     * @property player 执行蓄力操作的玩家。
     * @property itemStack 正在蓄力的物品堆。
     * @property hand 玩家使用的装备槽位。
     */
    class ChargeInfo(val player: Player, val itemStack: ItemStack, val hand: EquipmentSlot) {

        /**
         * 表示蓄力的开始时间。
         */
        val startTime = System.currentTimeMillis()

        /**
         * 表示蓄力的结束时间。
         */
        var stopTime = -1L

        /**
         * 获取已经蓄力的时间
         */
        val chargeTime: Long
            get() = if (stopTime == -1L) System.currentTimeMillis() - startTime else stopTime - startTime
    }
}