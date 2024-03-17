dependencies {
    // 引入 API
    compileOnly(project(":project:common"))
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")

    compileOnly("paper:v12004:12004:core")
    compileOnly("ink.ptms.core:v12001:12001:mapped")
    compileOnly("ink.ptms.core:v11802:11802:mapped")
    compileOnly("ink.ptms.core:v11802:11802:universal")
}

// 子模块
taboolib { subproject = true }