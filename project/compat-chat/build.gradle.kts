dependencies {
    // 引入 API
    compileOnly(project(":project:common"))
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")

    compileOnly("public:TrChat:2.0.4")
    compileOnly("public:InteractiveChat:4.2.7.2")

    compileOnly("paper:v12004:12004:core")
    compileOnly("ink.ptms.core:v12001:12001:mapped")
}

// 子模块
taboolib { subproject = true }