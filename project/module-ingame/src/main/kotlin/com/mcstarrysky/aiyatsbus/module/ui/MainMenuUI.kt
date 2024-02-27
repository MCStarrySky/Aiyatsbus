package com.mcstarrysky.aiyatsbus.module.ui

import com.mcstarrysky.aiyatsbus.core.StandardPriorities
import com.mcstarrysky.aiyatsbus.core.mechanism.Reloadable
import com.mcstarrysky.aiyatsbus.core.mechanism.Reloadables
import com.mcstarrysky.aiyatsbus.module.ui.internal.*
import com.mcstarrysky.aiyatsbus.module.ui.internal.config.MenuConfiguration
import com.mcstarrysky.aiyatsbus.module.ui.internal.feature.util.MenuFunctionBuilder
import org.bukkit.entity.Player
import taboolib.module.chat.component
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Chest
import com.mcstarrysky.aiyatsbus.module.ui.internal.registry.MenuFunctions
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.registerLifeCycleTask

@MenuComponent("Menu")
object MainMenuUI {

    @Config("ui/menu.yml")
    private lateinit var source: Configuration
    private lateinit var config: MenuConfiguration

    @Reloadable
    fun reload() {
        source.reload()
        config = MenuConfiguration(source)
    }

    fun open(player: Player) {
        player.record(UIType.MAIN_MENU)
        player.openMenu<Chest>(config.title().component().buildColored().toRawMessage()) {
            virtualize()
            val (shape, templates) = config
            rows(shape.rows)
            map(*shape.array)

            load(shape, templates, player)
        }
    }

    @MenuComponent
    private val enchant_search = MenuFunctionBuilder { onClick { (_, _, _, event, _) -> EnchantSearchUI.open(event.clicker) } }

    @MenuComponent
    private val item_check = MenuFunctionBuilder { onClick { (_, _, _, event, _) -> ItemCheckUI.open(event.clicker, null, ItemCheckUI.CheckMode.LOAD) } }

    @MenuComponent
    private val anvil = MenuFunctionBuilder { onClick { (_, _, _, event, _) -> AnvilUI.open(event.clicker) } }

    @Awake(LifeCycle.CONST)
    fun init() {
        registerLifeCycleTask(LifeCycle.ENABLE, StandardPriorities.MENU) {
            MenuFunctions.unregister("Back")
            MenuFunctions.register("Back", false) { back }
            Reloadables.execute()
        }
    }
}