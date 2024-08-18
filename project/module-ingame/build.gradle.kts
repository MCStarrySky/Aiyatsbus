dependencies {
    // 引入 API
    compileOnly(project(":project:common"))
    compileOnly("com.mojang:authlib:1.5.25")
}

// 子模块
taboolib { subproject = true }