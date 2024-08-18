dependencies {
    compileOnly(project(":project:common"))
    // 旧版本自定义附魔注册器
    compileOnly(project(":project:module-registration:registration-legacy"))
}

// 子模块
taboolib { subproject = true }