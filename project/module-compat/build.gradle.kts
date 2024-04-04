dependencies {
    // 引入 API
    compileOnly(project(":project:common"))

    compileOnly("public:Residence:minimize") // Residence
}

// 子模块
taboolib { subproject = true }