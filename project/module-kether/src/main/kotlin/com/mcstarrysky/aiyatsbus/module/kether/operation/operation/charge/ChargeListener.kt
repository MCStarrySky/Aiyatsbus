package com.mcstarrysky.aiyatsbus.module.kether.operation.operation.charge

import com.mcstarrysky.aiyatsbus.core.event.AiyatsbusBowChargeEvent
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.PacketReceiveEvent

/**
 * @author 坏黑
 */
object ChargeListener {

    /**
     * 处理接收到的数据包事件。
     * 此方法负责处理与蓄力相关的数据包，包括开始蓄力和释放蓄力。
     */
    @SubscribeEvent
    private fun onReceive(e: PacketReceiveEvent) {
        when (e.packet.name) {
            // Prepare shoot
            "PacketPlayInUseItem", "PacketPlayInUseEntity", "PacketPlayInBlockPlace" -> {
                // 手持弓
                val chargeHand = ChargeHandler.canCharge(e.player)
                if (chargeHand != null) {
                    e.isCancelled = true
                    // 设置手部活动状态
                    ChargeHandler.setHandActive(e.player, true)
                    // 储存信息（开始蓄力）
                    ChargeHandler.chargeInfo[e.player.name] = AiyatsbusBowChargeEvent.ChargeInfo(e.player, e.player.inventory.getItem(chargeHand)!!, chargeHand)
                }
            }
            // Release Item
            "PacketPlayInBlockDig" -> {
                // 判定动作
                if (e.packet.read<Any>("action").toString() == "RELEASE_USE_ITEM") {
                    val info = ChargeHandler.chargeInfo[e.player.name] ?: return
                    e.isCancelled = true
                    // 事件
                    AiyatsbusBowChargeEvent.Released(e.player, info.itemStack, info.hand, info).call()
                    // 结束蓄力
                    ChargeHandler.cancel(e.player)
                }
            }
        }
    }

    /**
     * 被攻击时打断蓄力
     */
    @SubscribeEvent
    private fun onDamaged(e: EntityDamageEvent) {
        val player = e.entity as? Player ?: return
        val info = ChargeHandler[player] ?: return
        if (AiyatsbusBowChargeEvent.Break(player, info, AiyatsbusBowChargeEvent.Break.Reason.DAMAGED, e).call()) {
            ChargeHandler.cancel(player)
        }
    }
}