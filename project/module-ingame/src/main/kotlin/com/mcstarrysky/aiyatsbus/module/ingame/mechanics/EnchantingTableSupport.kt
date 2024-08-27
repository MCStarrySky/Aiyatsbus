package com.mcstarrysky.aiyatsbus.module.ingame.mechanics

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.data.CheckType
import com.mcstarrysky.aiyatsbus.core.util.calcToDouble
import com.mcstarrysky.aiyatsbus.core.util.calcToInt
import com.mcstarrysky.aiyatsbus.core.util.serialized
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.round
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.enchantment.PrepareItemEnchantEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.common.util.randomDouble
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.conversion

@ConfigNode(bind = "core/mechanisms/enchanting_table.yml")
object EnchantingTableSupport {

    /**
     * 记录的等级 位置 to 等级
     */
    private val shelfAmount = mutableMapOf<String, Int>()

    @Config("core/mechanisms/enchanting_table.yml", autoReload = true)
    lateinit var conf: Configuration
        private set

    /**
     * 是否开启原版附魔台(无法从附魔台获得更多附魔)
     */
    @ConfigNode("vanilla_table")
    var vanillaTable = false

    /**
     * 出货数量概率
     */
    @ConfigNode("more_enchant_chance")
    var moreEnchantChance = listOf("0.2*{cost}", "0.15*{cost}", "0.1*{cost}")

    /**
     * 出货等级公示
     */
    @ConfigNode("level_formula")
    var levelFormula = "{cost}/3*{max_level}+{cost}*({random}-{random})"

    /**
     * 有此权限的玩家附魔必出满级附魔
     */
    @ConfigNode("privilege.full_level")
    var fullLevelPrivilege = "aiyatsbus.privilege.table.full"

    /**
     * 更多附魔数量的特权
     */
    @delegate:ConfigNode("privilege.chance")
    val moreEnchantPrivilege by conversion<List<String>, Map<String, String>> {
        mapOf(*map { it.split(":")[0] to it.split(":")[1] }.toTypedArray())
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun prepareEnchant(event: PrepareItemEnchantEvent) {
        if (vanillaTable)
            return
        // 记录附魔台的书架等级
        shelfAmount[event.enchantBlock.location.serialized] = event.enchantmentBonus.coerceAtMost(16)
    }

    @SubscribeEvent(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun doEnchant(event: EnchantItemEvent) {
        if (vanillaTable)
            return

        val player = event.enchanter
        val item = event.item.clone()
        val cost = event.whichButton() + 1
        val bonus = shelfAmount[event.enchantBlock.location.serialized] ?: 1

        // 书附魔完变成附魔书
        if (item.type == Material.BOOK) item.type = Material.ENCHANTED_BOOK

        // 首先获取附魔悬停信息上显示的附魔和等级, 并向物品添加, 因为这是必得的附魔
        val enchantmentHintLevel = if (player.hasPermission(fullLevelPrivilege)) {
            event.enchantmentHint.maxLevel
        } else event.enchantsToAdd[event.enchantmentHint]!!
        item.addEt(event.enchantmentHint.aiyatsbusEt, enchantmentHintLevel)

        // 附魔
        val result = doEnchant(player, item, cost, bonus)

        // 清空附魔列表
        event.enchantsToAdd.clear()
        // 添加悬停信息上的附魔
        event.enchantsToAdd += event.enchantmentHint to enchantmentHintLevel
        // 添加随机出来的附魔
        event.enchantsToAdd.putAll(result.first.mapKeys { it.key as Enchantment })

        // 对书的附魔，必须手动进行，因为原版处理会掉特殊附魔
        // 也许可以用更好的方法兼容，submit 有一定风险 FIXME
        if (item.type == Material.BOOK) {
            submit {
                event.inventory.setItem(0, result.second)
            }
        }
    }

    /**
     * 对物品进行附魔, 并返回新增的附魔列表和结果物品
     */
    private fun doEnchant(
        player: Player,
        item: ItemStack,
        cost: Int,
        bonus: Int
    ): Pair<Map<AiyatsbusEnchantment, Int>, ItemStack> {
        val enchantsToAdd = mutableMapOf<AiyatsbusEnchantment, Int>()
        val result = item.clone()

        // 额外出的货的数量
        val amount = calculateAmount(player, cost)
        // 选取的附魔范围
        val pool = result.etsAvailable(CheckType.ATTAIN, player).filter { it.alternativeData.isDiscoverable }

        repeat(amount) {
            // 从特定附魔列表中根据品质和附魔的权重抽取一个附魔
            val enchant = pool.drawEt() ?: return@repeat
            val maxLevel = enchant.basicData.maxLevel

            val level = if (player.hasPermission(fullLevelPrivilege)) maxLevel else levelFormula.calcToInt(
                "bonus" to bonus,
                "max_level" to maxLevel,
                "cost" to cost,
                "random" to Math.random().round(3)
            ).coerceIn(1, maxLevel)

            // 如果不与现有附魔冲突就添加
            if (enchant.limitations.checkAvailable(CheckType.ATTAIN, result, player).isSuccess) {
                result.addEt(enchant)
                enchantsToAdd[enchant] = level
            }
        }

        return enchantsToAdd to result
    }

    /**
     * 计算额外出的货的数量
     * 附魔台逻辑: 保留原本玩家悬停在附魔按钮上显示的附魔 + enchantAmount(player, cost) 个额外附魔
     */
    private fun calculateAmount(player: Player, cost: Int): Int {
        var count = 0
        for (formula in moreEnchantChance) {
            if (randomDouble() <= calculateChance(formula.calcToDouble("cost" to cost), player)) {
                count++
            } else {
                break
            }
        }
        return count
    }

    /**
     * 计算出货数量的概率, 应用特权
     */
    private fun calculateChance(origin: Double, player: Player) = moreEnchantPrivilege.maxOf { (perm, expression) ->
        if (player.hasPermission(perm)) expression.calcToDouble("chance" to origin) else origin
    }.coerceAtLeast(0.0)
}