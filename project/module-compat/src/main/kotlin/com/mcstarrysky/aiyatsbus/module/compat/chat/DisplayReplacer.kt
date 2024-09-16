package com.mcstarrysky.aiyatsbus.module.compat.chat

import com.mcstarrysky.aiyatsbus.module.compat.chat.display.DisplayReplacerDataComponents
import com.mcstarrysky.aiyatsbus.module.compat.chat.display.DisplayReplacerNBT
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import taboolib.common.util.unsafeLazy
import taboolib.module.nms.MinecraftVersion

/**
 * 用以替换 Component 中的 HoverEvent，使之支持更多附魔展示
 *
 * @author mical
 * @since 2024/8/18 16:36
 */
interface DisplayReplacer {

    /**
     * 替换 Component 中的 HoverEvent
     */
    fun apply(component: Component, player: Player): Component

    companion object {

        val inst by unsafeLazy {
            if (MinecraftVersion.majorLegacy >= 12005) {
                DisplayReplacerDataComponents
            } else {
                DisplayReplacerNBT
            }
        }
    }
}