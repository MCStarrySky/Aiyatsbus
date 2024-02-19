package com.mcstarrysky.aiyatsbus.core

import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
 *
 * @author mical
 * @since 2024/2/17 14:31
 */
object AiyatsbusSettings {

    @Config("config.yml", autoReload = true)
    lateinit var conf: Configuration
        private set

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
    @ConfigNode("Settings.anti-illegal-item.check-list")
    var antiIllegalItemCheckList = emptyList<String>()
}