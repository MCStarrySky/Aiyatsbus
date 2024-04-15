package com.mcstarrysky.aiyatsbus.core

import com.mcstarrysky.aiyatsbus.core.data.LimitType
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.conversion

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
 *
 * @author mical
 * @since 2024/2/17 14:31
 */
@ConfigNode(bind = "core/config.yml")
object AiyatsbusSettings {

    @Config("core/config.yml", autoReload = true)
    lateinit var conf: Configuration
        private set

    /**
     * 是否在管理员登录游戏时发送鸣谢信息
     */
    @ConfigNode("Settings.send-thank-messages")
    var sendThankMessages = true

    /**
     * 自动释放插件内置的附魔包
     */
    @ConfigNode("Settings.auto-release-enchants")
    var autoReleaseEnchants = true

    /**
     * 默认品质
     */
    @ConfigNode("Settings.default-rarity")
    var defaultRarity = "common"

    /**
     * 附魔主菜单返回按钮的指令
     */
    @ConfigNode("Settings.main-menu-back")
    var mainMenuBack = "cd"

    /**
     * 是否开启自动修改玩家背包内非法物品
     */
    @ConfigNode("Settings.anti-illegal-item.enable")
    var enableAntiIllegalItem = false

    /**
     * 自动修改玩家背包内非法物品的检测时间间隔
     */
    @ConfigNode("Settings.anti-illegal-item.interval")
    var antiIllegalItemInterval = 60L

    /**
     * 自动修改玩家背包内非法物品的检测列表
     */
    @delegate:ConfigNode("Settings.anti-illegal-item.check-list")
    val antiIllegalItemCheckList by conversion<List<String>, List<LimitType>> {
        toTypedArray().map(LimitType::valueOf)
    }

    /**
     * 是否开启 ItemsAdder 支持
     */
    @ConfigNode("Settings.support-items-adder")
    var supportItemsAdder = false

    /**
     * 是否开启脚本预热
     */
    @ConfigNode("Settings.enable-kether-preheat")
    var enableKetherPreheat = true

    /**
     * 是否开启性能检测工具
     */
    @ConfigNode("Settings.enable-performance-tool")
    var enablePerformanceTool = false

    /**
     * 是否显示性能监控的详情信息
     */
    @ConfigNode("Settings.show-performance-details")
    var showPerformanceDetails = false

    /**
     * 对于附魔的挖掘放置攻击生物等操作, OP 是否无视领地等保护
     */
    @ConfigNode("Settings.anti-grief-ignore-op")
    var antiGriefIgnoreOp = true

    /**
     * 若没有特殊设置, 将冷却消息显示在动作栏而不是聊天框
     */
    @ConfigNode("Settings.cool-down-in-actionbar")
    var coolDownInActionBar = false
}

const val AIYATSBUS_PREFIX = "&8[&{#D8D8FA}Aiyatsbus&8]&7"