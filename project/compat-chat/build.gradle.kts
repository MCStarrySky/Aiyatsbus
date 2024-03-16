dependencies {
    // 引入 API
    compileOnly(project(":project:common"))
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")

    compileOnly("public:TrChat:2.0.4")
    compileOnly("public:InteractiveChat:4.2.7.2")

    compileOnly("paper:v12004:12004:core")
}

// 子模块
taboolib { subproject = true }