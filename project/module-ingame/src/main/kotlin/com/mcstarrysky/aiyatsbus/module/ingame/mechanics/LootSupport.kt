package com.mcstarrysky.aiyatsbus.module.ingame.mechanics

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.data.CheckType
import com.mcstarrysky.aiyatsbus.core.util.MathUtils.preheatExpression
import com.mcstarrysky.aiyatsbus.core.util.calcToDouble
import com.mcstarrysky.aiyatsbus.core.util.calcToInt
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.world.LootGenerateEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.conversion
import kotlin.math.roundToInt

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.ingame.mechanics.LootSupport
 *
 * @author mical
 * @since 2024/5/1 23:20
 */
@ConfigNode(bind = "core/mechanisms/loot.yml")
object LootSupport {

    @Config("core/mechanisms/loot.yml", autoReload = true)
    lateinit var conf: Configuration
        private set

    @ConfigNode("enable")
    var enable = true
    @delegate:ConfigNode("more_enchant_chance")
    val moreEnchantChance by conversion<List<String>, List<String>> {
        this.onEach { it.preheatExpression() }
    }
    @delegate:ConfigNode("level_formula")
    val levelFormula by conversion<String, String> {
        preheatExpression()
    }
    @ConfigNode("privilege.full_level")
    var fullLevelPrivilege = "aiyatsbus.privilege.table.full"
    @ConfigNode("cost")
    var cost = 3
    @ConfigNode("bonus")
    var bonus = 16

    @delegate:ConfigNode("privilege.chance")
    val moreEnchantPrivilege by conversion<List<String>, Map<String, String>> {
        mapOf(*map { it.split(":")[0] to it.split(":")[1] }.toTypedArray())
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onLootGenerate(event: LootGenerateEvent) {
        if (!enable) return
        (event.entity as? Player)?.let {
            event.loot.replaceAll { item ->
                if (item.fixedEnchants.isNotEmpty()) enchant(it, ItemStack(item.type)).second
                else item
            }
        } ?: event.loot.removeIf { it.fixedEnchants.isNotEmpty() }
    }

    private fun enchant(
        player: Player,
        item: ItemStack,
        cost: Int = this.cost,
        bonus: Int = this.bonus,
        checkType: CheckType = CheckType.ATTAIN
    ): Pair<Map<AiyatsbusEnchantment, Int>, ItemStack> {
        val enchantsToAdd = mutableMapOf<AiyatsbusEnchantment, Int>()
        val result = item.clone()
        if (item.type == Material.BOOK) result.type = Material.ENCHANTED_BOOK

        val amount = enchantAmount(player, cost)
        val pool = result.etsAvailable(checkType, player).filter { !it.alternativeData.isTreasure }

        repeat(amount) {
            val enchant = pool.drawEt() ?: return@repeat
            val maxLevel = enchant.basicData.maxLevel
            val level = if (player.hasPermission(fullLevelPrivilege)) maxLevel
            else levelFormula.calcToInt("bonus" to bonus, "max_level" to maxLevel, "button" to cost)
                .coerceAtLeast(1)
                .coerceAtMost(maxLevel)

            if (enchant.limitations.checkAvailable(checkType, result, player).isSuccess) {
                result.addEt(enchant)
                enchantsToAdd[enchant] = level
            }
        }

        return enchantsToAdd to result
    }

    private fun enchantAmount(player: Player, cost: Int) = moreEnchantChance.count {
        Math.random() <= finalChance(it.calcToDouble("button" to cost), player)
    }.coerceAtLeast(1)

    private fun finalChance(origin: Double, player: Player) = moreEnchantPrivilege.maxOf { (perm, expression) ->
        if (player.hasPermission(perm)) expression.calcToInt("chance" to origin)
        else origin.roundToInt()
    }.coerceAtLeast(0)
}