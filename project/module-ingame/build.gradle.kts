dependencies {
    // 引入 API
    compileOnly(project(":project:common"))
}

// 子模块
taboolib { subproject = true }