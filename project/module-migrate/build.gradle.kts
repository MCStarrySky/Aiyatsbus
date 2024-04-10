dependencies {
    // 引入 API
    compileOnly(project(":project:common"))
    compileOnly("public:NereusOpus:minimize") // NereusOpus
}

// 子模块
taboolib { subproject = true }