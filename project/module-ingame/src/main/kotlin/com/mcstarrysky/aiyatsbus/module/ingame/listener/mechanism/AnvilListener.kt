package com.mcstarrysky.aiyatsbus.module.ingame.listener.mechanism

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.data.CheckType
import com.mcstarrysky.aiyatsbus.core.util.calcToDouble
import com.mcstarrysky.aiyatsbus.core.util.calcToInt
import com.mcstarrysky.aiyatsbus.core.util.damage
import com.mcstarrysky.aiyatsbus.core.util.name
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common5.cdouble
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.conversion
import kotlin.math.ceil
import kotlin.math.roundToInt

@ConfigNode(bind = "core/mechanisms/anvil.yml")
object AnvilListener {

    @Config("core/mechanisms/anvil.yml", autoReload = true)
    lateinit var conf: Configuration
        private set

    @ConfigNode("limit.unsafe_level")
    var allowUnsafeLevel = true

    @ConfigNode("limit.unsafe_combine")
    var allowUnsafeCombine = false

    @ConfigNode("max_cost")
    var maxCost = 100

    @ConfigNode("rename_cost")
    var renameCost = 3

    @ConfigNode("repair_cost")
    var repairCost = 5

    @ConfigNode("enchant_cost.new_extra")
    var newEnchantExtraCost = 2

    @ConfigNode("enchant_cost.per_level")
    var enchantCostPerLevel = "6.0/{max_level}"

    @ConfigNode("allow_different_material")
    var allowDifferentMaterial = false

    @delegate:ConfigNode("privilege")
    val privilege by conversion<List<String>, Map<String, String>> {
        mapOf(*toTypedArray().map { it.split(":")[0] to it.split(":")[1] }.toTypedArray())
    }

    @SubscribeEvent(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun anvil(event: PrepareAnvilEvent) {
        val inv = event.inventory
        val player = event.viewers[0] as Player

        val a = inv.firstItem ?: return
        val b = inv.secondItem

        var renameText = event.result?.name
        if (renameText?.isBlank() != false)
            renameText = inv.renameText

        val result = anvil(a, b, player, renameText)

        result.first ?: let {
            event.result = null
            inv.result = null
            return
        }
        event.result = result.first
        inv.repairCost = result.second
        inv.repairCostAmount = result.third
        inv.maximumRepairCost = 100
    }

    //第三个数据是消耗的修复物品数量
    fun anvil(a: ItemStack, b: ItemStack?, player: Player, name: String? = null): Triple<ItemStack?, Int, Int> {
        val typeA = a.type
        val typeB = b?.type ?: Material.AIR

        val result = a.clone()
        var cost = 0.0

        var amount = 1

        name?.let {
            if (it.isNotBlank()) {
                result.name = it
                cost += renameCost
            }
        }
        if (b != null)
            if ((b.canRepair(a) || typeB == typeA) && a.type.maxDurability != 0.toShort()) {
                val pair = durabilityFixed(typeA, typeB, b.amount, a.damage, b.damage)
                result.damage = maxOf(0, result.damage - pair.first)
                cost += repairCost
                amount = pair.second
            }

        if (typeA == typeB || typeB == Material.ENCHANTED_BOOK ||
            (allowDifferentMaterial && typeB.belongedTargets.any { !typeA.belongedTargets.contains(it) })
        ) {
            val tmp = a.clone()
            b!!.fixedEnchants.filterKeys {
                val checked = it.limitations.checkAvailable(CheckType.ANVIL, tmp, player)
                if (checked.first) tmp.addEt(it, b.etLevel(it))
                checked.first
            }.forEach { (enchant, lv) ->
                println(enchant)
                val old = a.etLevel(enchant)
                val new = if (old < lv) {
                    if (old <= 0) cost += newEnchantExtraCost
                    if (lv > enchant.basicData.maxLevel && !allowUnsafeLevel) return@forEach
                    lv
                } else if (old == lv) {
                    if (old >= enchant.basicData.maxLevel && !allowUnsafeCombine) return@forEach
                    lv + 1
                } else return@forEach
                result.addEt(enchant, new)
                cost += enchantCostPerLevel.calcToDouble("max_level" to enchant.basicData.maxLevel) * (new - old.coerceAtLeast(0))
            }
        }

        if (cost == 0.0 || result == a)
            return Triple(null, 0, 0)

        return Triple(result, finalCost(cost, player), amount)
    }

    fun durabilityFixed(type: Material, fixer: Material, amount: Int, dmgA: Int, dmgB: Int): Pair<Int, Int> {
        val typeS = type.toString()
        val fixerS = fixer.toString()
        val isArmor = typeS.contains("HELMET") ||
                typeS.contains("CHESTPLATE") ||
                typeS.contains("LEGGINGS") ||
                typeS.contains("BOOTS") ||
                type == Material.SHIELD
        var fixed = -1
        if (typeS.startsWith("WOODEN_") && fixerS.endsWith("PLANKS"))
            fixed = if (isArmor) 84 else 14
        if (typeS.startsWith("STONE_") && (fixer == Material.BLACKSTONE || fixer == Material.COBBLESTONE || fixer == Material.DEEPSLATE))
            fixed = 32
        if (typeS.startsWith("IRON_") && fixer == Material.IRON_INGOT)
            fixed = if (isArmor) 56 else 62
        if (typeS.startsWith("DIAMOND_") && fixer == Material.DIAMOND)
            fixed = if (isArmor) 123 else 390
        if (typeS.startsWith("NETHERITE_") && fixer == Material.NETHERITE_INGOT)
            fixed = if (isArmor) 138 else 507
        if (typeS.startsWith("GOLD_") && fixer == Material.GOLD_INGOT)
            fixed = if (isArmor) 26 else 8
        if (typeS.startsWith("LEATHER_") && fixer == Material.LEATHER)
            fixed = 18
        if (type == Material.ELYTRA && fixer == Material.PHANTOM_MEMBRANE)
            fixed = 108
        if (type == Material.TURTLE_HELMET && fixer == Material.SCUTE)
            fixed = 68
        if (type == fixer) {
            fixed = fixer.maxDurability - dmgB
        }

        val minAmount = minOf(ceil(dmgA.cdouble / fixed).roundToInt(), amount)
        return (fixed * minAmount).coerceAtMost(type.maxDurability.toInt()) to minAmount
    }

    fun finalCost(origin: Double, player: Player) = privilege.minOf { (perm, expression) ->
        if (player.hasPermission(perm)) expression.calcToInt("cost" to origin)
        else origin.roundToInt()
    }.coerceAtMost(maxCost).coerceAtLeast(1)
}