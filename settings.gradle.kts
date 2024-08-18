rootProject.name = "Aiyatsbus"

include("plugin")
include("project:common")
// 实现
include("project:common-impl")
// 运行平台
include("project:module-bukkit")
// 与其他插件兼容模块
include("project:module-compat")
// 玩家交互代码
include("project:module-ingame")
// 脚本
include("project:module-kether")
// 语言系统
include("project:module-language")
// NMS 实现
include("project:module-nms:nms-common")
// 1.20.5 NMS 实现
include("project:module-nms:nms-v12005")
// 旧版本自定义附魔注册器
include("project:module-registration:registration-legacy")
// 1.20.4 自定义附魔注册器
include("project:module-registration:registration-v12004-paper")
include("project:module-registration:registration-v12004-vanilla")
// 1.21 自定义附魔注册器
include("project:module-registration:registration-v12100-paper")
include("project:module-registration:registration-v12100-vanilla")