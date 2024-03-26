@file:Suppress("DEPRECATION")

package com.mcstarrysky.aiyatsbus.module.ingame.ui

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.data.CheckType
import com.mcstarrysky.aiyatsbus.core.data.LimitType
import com.mcstarrysky.aiyatsbus.core.data.MenuMode
import com.mcstarrysky.aiyatsbus.core.util.isNull
import com.mcstarrysky.aiyatsbus.core.util.name
import com.mcstarrysky.aiyatsbus.core.util.roman
import com.mcstarrysky.aiyatsbus.core.util.subList
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.*
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.feature.util.MenuFunctionBuilder
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.variable
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.variables
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot.*
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import taboolib.module.chat.component
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.PageableChest
import taboolib.platform.util.giveItem
import taboolib.platform.util.modifyLore
import taboolib.platform.util.modifyMeta
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.UIType
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.record
import kotlin.collections.set

@MenuComponent("EnchantInfo")
object EnchantInfoUI {

    @Config("core/ui/enchant_info.yml")
    private lateinit var source: Configuration
    private lateinit var config: MenuConfiguration

    fun reload() {
        source.reload()
        config = MenuConfiguration(source)
    }

    fun open(player: Player, params: Map<String, Any?>) {
        open(
            player, params["enchant"] as AiyatsbusEnchantment,
            params["level"] as Int,
            params["checked"] as ItemStack,
            params["category"].toString()
        )
    }

    fun open(
        player: Player,
        enchant: AiyatsbusEnchantment,
        level: Int = enchant.basicData.maxLevel,
        checked: ItemStack = ItemStack(Material.AIR),
        category: String = "conflicts"
    ) {
        if (player.menuMode == MenuMode.CHEAT) {
            player.giveItem(enchant.book(level))
            return
        }

        player.record(UIType.ENCHANT_INFO, "enchant" to enchant, "level" to level, "checked" to checked, "category" to category)
        player.openMenu<PageableChest<String>>(
            config.title()
                .replace("[enchant_display_roman]", enchant.displayName(level))
                .component().buildColored().toRawMessage()
        ) {
            virtualize()

            val (shape, templates) = config
            rows(shape.rows)
            val slots = shape["EnchantInfo:element"].toList()
            slots(slots)
            elements {
                when (category) {
                    "conflicts" -> enchant.limitations.limitations.mapNotNull { (type, identifier) ->
                        when (type) {
                            LimitType.CONFLICT_ENCHANT -> "enchant:$identifier"
                            LimitType.CONFLICT_GROUP -> "group:$identifier"
                            else -> null
                        }
                    }

                    "dependencies" -> enchant.limitations.limitations.mapNotNull { (type, identifier) ->
                        when (type) {
                            LimitType.DEPENDENCE_ENCHANT -> "enchant:$identifier"
                            LimitType.DEPENDENCE_GROUP -> "group:$identifier"
                            else -> null
                        }
                    }

                    "related" -> aiyatsbusGroups.values.filter { enchant.enchantment.isInGroup(it) }.map { "group:${it.name}" }
                    else -> listOf()
                }
            }

            load(
                shape, templates, player,
                "EnchantInfo:available", "EnchantInfo:other", "EnchantInfo:limitations",
                "EnchantInfo:basic", "EnchantInfo:level", "EnchantInfo:related",
                "EnchantInfo:dependencies", "EnchantInfo:conflicts", "EnchantInfo:element",
                "EnchantInfo:favorite", "EnchantInfo:minus", "EnchantInfo:plus", "Previous", "Next"
            )
            pages(shape, templates)

            val template = templates.require("EnchantInfo:element")
            onGenerate { _, element, index, slot -> template(slot, index) { this["element"] = element;this["category"] = category } }
            onClick { event, element -> templates[event.rawSlot]?.handle(this, event, "element" to element) }

            val params = arrayOf(
                "player" to player,
                "enchant" to enchant,
                "level" to level,
                "checked" to checked,
                "category" to category
            )
            listOf("conflicts", "dependencies", "related").forEach {
                setSlots(shape, templates, "EnchantInfo:$it", listOf(), *params.clone().also { p -> p[4] = "category" to it })
            }
            setSlots(shape, templates, "EnchantInfo:available", listOf(), *params)
            setSlots(shape, templates, "EnchantInfo:basic", listOf(), *params)
            setSlots(shape, templates, "EnchantInfo:limitations", listOf(), *params)
            setSlots(shape, templates, "EnchantInfo:other", listOf(), *params)
            setSlots(shape, templates, "EnchantInfo:favorite", listOf(), *params)
            setSlots(shape, templates, "EnchantInfo:level", listOf(), *params)
            setSlots(shape, templates, "EnchantInfo:minus", listOf(), *params)
            setSlots(shape, templates, "EnchantInfo:plus", listOf(), *params)

            onClick { event ->
                event.isCancelled = true
                if (event.rawSlot !in shape) {
                    val item = event.currentItem ?: return@onClick
                    open(player, enchant, level, item, category)
                }
            }
        }
    }

    @MenuComponent
    private val conflicts = MenuFunctionBuilder { onClick { (_, _, _, event, args) -> open(event.clicker, args) } }

    @MenuComponent
    private val dependencies = MenuFunctionBuilder { onClick { (_, _, _, event, args) -> open(event.clicker, args) } }

    @MenuComponent
    private val related = MenuFunctionBuilder { onClick { (_, _, _, event, args) -> open(event.clicker, args) } }

    @MenuComponent
    private val level = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val level = args["level"] as Int
            val enchant = args["enchant"] as AiyatsbusEnchantment
            icon.amount = level
            icon.variables {
                when (it) {
                    "params" -> enchant.variables.leveled.map { (variable) ->
                        "&b$variable &7> " + enchant.variables.leveled(variable, level, true)
                    }

                    "roman" -> listOf(level.roman())
                    else -> listOf()
                }
            }
        }
        onClick { (_, _, _, event, args) ->
            val enchant = args["enchant"] as AiyatsbusEnchantment
            open(event.clicker, args.toMutableMap().also { it["level"] = enchant.basicData.maxLevel })
        }
    }

    @MenuComponent
    private val minus = MenuFunctionBuilder {
        onClick { (_, _, _, event, args) ->
            val level = args["level"] as Int
            open(event.clicker, args.toMutableMap().also { it["level"] = (level - 1).coerceAtLeast(1) })
        }
    }

    @MenuComponent
    private val plus = MenuFunctionBuilder {
        onClick { (_, _, _, event, args) ->
            val level = args["level"] as Int
            val enchant = args["enchant"] as AiyatsbusEnchantment
            open(event.clicker, args.toMutableMap().also { it["level"] = (level + 1).coerceAtMost(enchant.basicData.maxLevel) })
        }
    }


    @MenuComponent
    private val basic = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val enchant = args["enchant"] as AiyatsbusEnchantment
            val holders = enchant.displayer.holders(enchant.basicData.maxLevel)
            icon.variables { variable -> listOf(holders[variable] ?: "") }
                .skull(enchant.rarity.skull)
        }
    }

    @MenuComponent
    private val limitations = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val enchant = args["enchant"] as AiyatsbusEnchantment
            val player = args["player"] as Player
            val limits = enchant.limitations.limitations
            val conflicts = limits.filter { it.first.toString().contains("CONFLICT") }.joinToString("; ") { it.second }
            val dependencies = limits.filter { it.first.toString().contains("DEPENDENCE") }.joinToString("; ") { it.second }
            val perms = limits.filter { it.first == LimitType.PERMISSION }.map { it.second }
            val permission = if (perms.none { !player.hasPermission(it) }) "&a✓" else "&c✗"
            val activeSlots = enchant.targets.map { it.activeSlots }.flatten().toSet().joinToString("; ") {
                when (it) {
                    HAND -> "主手"
                    OFF_HAND -> "副手"
                    FEET -> "足"
                    LEGS -> "腿"
                    CHEST -> "胸"
                    HEAD -> "头"
                }
            }
            icon.variables {
                listOf(
                    when (it) {
                        "targets" -> enchant.targets.joinToString("; ") { target -> target.name }
                        "conflicts" -> conflicts
                        "dependencies" -> dependencies
                        "permission" -> permission
                        "disable_worlds" -> enchant.basicData.disableWorlds.joinToString("; ")
                        "active_slots" -> activeSlots
                        else -> ""
                    }.ifBlank { "删除本行" }
                )
            }.modifyLore {
                removeIf { it.contains("删除本行") }
            }
        }
    }

    @MenuComponent
    private val other = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val enchant = args["enchant"] as AiyatsbusEnchantment
            var attainWays = ""
            if (enchant.alternativeData.isDiscoverable &&
                enchant.alternativeData.weight > 0 &&
                enchant.rarity.weight > 0
            ) attainWays += "&d附魔台 &e战利品箱"
            if (enchant.alternativeData.isTradeable &&
                enchant.enchantment.isInGroup("可交易附魔")
            ) {
                if (attainWays.isNotBlank()) attainWays += " "
                attainWays += "&6村民"
            }
            icon.variables {
                listOf(
                    when (it) {
                        "attain_ways" -> attainWays
                        "grindstoneable" -> if (enchant.alternativeData.grindstoneable) "&a✓" else "&c✗"
                        "treasure" -> if (enchant.alternativeData.isTreasure) "&a✓" else "&c✗"
                        "curse" -> if (enchant.alternativeData.isCursed) "&a✓" else "&c✗"
                        else -> ""
                    }
                )
            }
        }
    }

    @MenuComponent
    private val available = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val enchant = args["enchant"] as AiyatsbusEnchantment
            val checked = args["checked"] as ItemStack
            if (checked.isNull) {
                return@onBuild icon.variables {
                    listOf(
                        when (it) {
                            "state" -> "N/A"
                            "reasons" -> "无"
                            else -> ""
                        }
                    )
                }
            }
            val level = checked.etLevel(enchant)
            val player = args["player"] as Player
            val result = enchant.limitations.checkAvailable(CheckType.ANVIL, checked, player)
            val state = if (level > 0) "&a已安装 " + if (level < enchant.basicData.maxLevel) "可升级" else "最高级"
            else if (result.first) "&e可安装"
            else "&c不可安装"
            val reasons = result.second.ifEmpty { "无" }

            icon.variables {
                listOf(
                    when (it) {
                        "state" -> state
                        "reasons" -> reasons
                        else -> ""
                    }
                )
            }
        }
    }

    @MenuComponent
    private val element = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val element = args["element"].toString()
            val parts = element.split(":")
            when (parts[0]) {
                "group" -> {
                    val group = aiyatsbusGroup(parts[1])!!
                    icon.name = icon.name!!.split("||")[0]
                    icon.skull(group.skull)
                        .modifyMeta<ItemMeta> {
                            val lore = lore!!
                            val index = lore.indexOf("分隔符号")
                            this.lore = lore.subList(0, index)
                        }.variables {
                            listOf(
                                when (it) {
                                    "group" -> group.name
                                    "amount" -> group.enchantments.size.toString()
                                    "max_coexist" -> if (args["category"] == "conflicts") "${group.maxCoexist - 1}" else "删除本行"
                                    else -> ""
                                }
                            )
                        }.modifyLore { removeIf { it.contains("删除本行") } }
                }

                "enchant" -> {
                    val enchant = aiyatsbusEt(parts[1])!!
                    val holders = enchant.displayer.holders(enchant.basicData.maxLevel)
                    icon.name = icon.name!!.split("||")[1]
                    icon.skull(enchant.rarity.skull)
                        .modifyMeta<ItemMeta> {
                            val lore = lore!!
                            val index = lore.indexOf("分隔符号")
                            this.lore = lore.subList(index + 1)
                        }.variables { variable -> listOf(holders[variable] ?: "") }
                }

                else -> icon
            }
        }

        onClick { (_, _, _, event, args) ->
            val player = event.clicker
            val element = args["element"].toString()
            val parts = element.split(":")
            val filter = Aiyatsbus.api().getEnchantmentFilter()
            when (parts[0]) {
                "group" -> {
                    filter.clearFilters(player)
                    filter.addFilter(player, FilterType.GROUP, parts[1], FilterStatement.ON)
                    EnchantSearchUI.open(player)
                }

                "enchant" -> open(player, aiyatsbusEt(parts[1])!!)
            }
        }
    }

    @MenuComponent
    private val favorite = MenuFunctionBuilder {
        onBuild { (_, _, _, _, icon, args) ->
            val enchant = args["enchant"] as AiyatsbusEnchantment
            val player = args["player"] as Player
            icon.variable(
                "state", listOf(
                    if (!player.favorites.contains(enchant.basicData.id)) "&a收藏"
                    else "&c移除收藏"
                )
            )
        }


        onClick { (_, _, _, event, args) ->
            val player = event.clicker
            val enchant = args["enchant"] as AiyatsbusEnchantment
            val id = enchant.basicData.id
            if (!player.favorites.contains(id)) player.favorites += id
            else player.favorites -= id

            open(player, args)
        }
    }
}