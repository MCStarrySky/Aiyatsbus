dependencies {
    // 引入 API
    compileOnly(project(":project:common"))
    compileOnly(project(":project:module-migrate"))

    compileOnly("com.mojang:authlib:1.5.25")
}

// 子模块
taboolib { subproject = true }