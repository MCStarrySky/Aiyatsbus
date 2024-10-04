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
package com.mcstarrysky.aiyatsbus.module.kether

import com.mcstarrysky.aiyatsbus.core.AiyatsbusKetherHandler
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.PlatformFactory
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.console
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions
import taboolib.module.kether.parseKetherScript
import taboolib.module.kether.runKether
import java.util.concurrent.CompletableFuture

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.DefaultAiyatsbusKetherHandler
 *
 * @author mical
 * @since 2024/3/9 18:30
 */
class DefaultAiyatsbusKetherHandler : AiyatsbusKetherHandler {

    override fun invoke(source: String, sender: CommandSender?, variables: Map<String, Any?>): CompletableFuture<Any?>? {
        val player = sender as? Player
        return runKether(detailError = true) {
            KetherShell.eval(source,
                ScriptOptions.builder().namespace(namespace = listOf("aiyatsbus"))
                    .sender(sender = if (player != null) adaptPlayer(player) else if (sender != null) adaptCommandSender(sender) else console())
                    .vars(variables)
                    .build())
        }
    }

    override fun preheat(source: String) {
        val s = if (source.startsWith("def ")) source else "def main = { $source }"
        KetherShell.mainCache.scriptMap[s] = s.parseKetherScript(listOf("aiyatsbus"))
    }

    companion object {

        @Awake(LifeCycle.CONST)
        fun init() {
            PlatformFactory.registerAPI<AiyatsbusKetherHandler>(DefaultAiyatsbusKetherHandler())
        }
    }
}