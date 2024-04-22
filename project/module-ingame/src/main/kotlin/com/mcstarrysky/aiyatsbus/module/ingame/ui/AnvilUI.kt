package com.mcstarrysky.aiyatsbus.module.ingame.ui

import com.mcstarrysky.aiyatsbus.core.asLang
import com.mcstarrysky.aiyatsbus.core.asLangList
import com.mcstarrysky.aiyatsbus.core.data.CheckType
import com.mcstarrysky.aiyatsbus.core.fixedEnchants
import com.mcstarrysky.aiyatsbus.core.util.isNull
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.MenuComponent
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature.util.MenuFunctionBuilder
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.variables
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.load
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.setSlots
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.chat.component
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Chest
import com.mcstarrysky.aiyatsbus.module.ingame.listener.mechanism.AnvilListener
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.UIType
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.variable
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.record
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

@MenuComponent("Anvil")
object AnvilUI {

    @Config("core/ui/anvil.yml", autoReload = true)
    private lateinit var source: Configuration
    private lateinit var config: MenuConfiguration

    fun reload() {
        source.reload()
        config = MenuConfiguration(source)
    }

    @Awake(LifeCycle.ENABLE)
    fun init() {
        source.onReload {
            config = MenuConfiguration(source)
        }
    }

    fun open(player: Player, a: ItemStack? = null, b: ItemStack? = null) {
        player.record(UIType.ANVIL, "a" to a, "b" to b)
        player.openMenu<Chest>(config.title().component().buildColored().toLegacyText()) {
//            virtualize()

            val (shape, templates) = config
            rows(shape.rows)
            map(*shape.array)

            load(shape, templates, player, "Anvil:a", "Anvil:b", "Anvil:result", "Anvil:information")

            val info = mutableMapOf<String, String>()
            var result: ItemStack? = ItemStack(Material.AIR)
            if (!a.isNull && !b.isNull) {
                val resultPair = AnvilListener.anvil(a!!, b!!, player)
                result = resultPair.first
                val cost = resultPair.second

                val canCombine = if (result == null || cost <= 0) false
                else !result.isSimilar(a)

                if (canCombine) {
                    info["allowed"] = player.asLang("ui-anvil-info-allow")
                    info["level"] = player.asLang("ui-anvil-info-level", cost to "cost")
                    info["reasons"] = player.asLangList("ui-anvil-info-reasons-empty").joinToString("[](br)")
                } else {
                    val bugs = b.fixedEnchants.mapNotNull { (enchant, _) ->
                        val check = enchant.limitations.checkAvailable(CheckType.ANVIL, a, player)
                        if (!check.first) check.second
                        else null
                    }
                    info["allowed"] = player.asLang("ui-anvil-info-disallow")
                    info["level"] = player.asLang("ui-anvil-info-level-disallow")
                    info["reasons"] = player.asLangList("ui-anvil-info-reasons").variable("reason", bugs).joinToString("[](br)")
                }
            }

            listOf("a", "b", "result", "information").forEach {
                setSlots(
                    shape, templates, "Anvil:$it", listOf(),
                    "a" to (a ?: ItemStack(Material.AIR)), "b" to (b ?: ItemStack(Material.AIR)),
                    "result" to (result ?: ItemStack(Material.AIR)), "info" to info
                )
            }

            onClick { event ->
                event.isCancelled = true
                if (event.rawSlot !in shape) {
                    val item = event.currentItem ?: return@onClick
                    if (a == null) open(player, item, b)
                    else if (b == null) open(player, a, item)
                }
            }
        }
    }

    @MenuComponent
    private val a = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) -> (args["a"] as ItemStack).takeIf { !it.isNull } ?: icon }
        onClick { (_, _, _, event, args) -> open(event.clicker, null, args["b"] as? ItemStack) }
    }

    @MenuComponent
    private val b = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) -> (args["b"] as ItemStack).takeIf { !it.isNull } ?: icon }
        onClick { (_, _, _, event, args) -> open(event.clicker, args["a"] as? ItemStack, null) }
    }

    @MenuComponent
    private val result = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) -> (args["result"] as ItemStack).takeIf { !it.isNull } ?: icon }
    }

    @MenuComponent
    private val information = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val tmp = args["info"] as Map<*, *>
            val info = tmp.mapKeys { it.key.toString() }.mapValues { it.value.toString() }
            if (info.isEmpty()) icon.variables { listOf("-") }
            else icon.variables {
                when (it) {
                    "allowed" -> listOf(info["allowed"]!!)
                    "level" -> listOf(info["level"]!!)
                    "reasons" -> info["reasons"]!!.split("[](br)")
                    else -> listOf()
                }
            }
        }
    }
}