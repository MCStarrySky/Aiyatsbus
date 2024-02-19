package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.chain

enum class ChainType(val display: String) {
    //在Listener层面处理
    DELAY("延时"), // 格式 - 延时::时间(秒)
    GOTO("跳转"), // 格式 - 跳转::步骤序号
    END("终止"), // 格式 - 终止

    //在Chain层面中处理
    TRAVERSE("遍历"), // 格式 - 遍历::列表对象:操作类型:子语句/参数
    COOLDOWN("冷却"), // 格式 - 冷却::时间(秒):是否通告给玩家
    CONDITION("条件"), // 格式 - 条件::布尔表达式
    ASSIGNMENT("赋值"), // 格式 - 赋值::变量名:值表达式
    EVENT("事件"), // 格式 - 事件::修改事件的指令:参数...
    OPERATION("操作"), // 格式 - 操作::已经注册的操作:参数...
    OBJECT("对象"), // 格式 - 对象::对象地址:修改对象的指令:参数...
    ITEM("物品"); // 格式 - 物品::修改指令:参数...

    companion object {
        fun getType(identifier: String?): ChainType? = ChainType.values().find { it.display == identifier || it.name == identifier }
    }
}