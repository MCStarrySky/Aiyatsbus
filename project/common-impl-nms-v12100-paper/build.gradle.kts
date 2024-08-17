dependencies {
    compileOnly(project(":project:common"))
    compileOnly("paper:v12101:12101:core")
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
}

// 子模块
taboolib { subproject = true }